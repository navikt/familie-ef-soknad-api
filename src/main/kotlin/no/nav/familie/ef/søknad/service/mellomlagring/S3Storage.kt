package no.nav.familie.ef.søknad.service.mellomlagring

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.CreateBucketRequest
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class S3Storage(private val s3: AmazonS3) : Storage {

    init {
        try {
            ensureBucketExists(BUCKET_ENSLIG_FORSØRGER, 365)
            ensureBucketExists(BUCKET_ENSLIG_FORSØRGER_MELLOMLAGRING, 1)
        } catch (ex: Exception) {
            LOG.error("Could not create S3 bucket", ex)
        }

    }

    override fun put(directory: String, key: String, value: String) {
        writeString(BUCKET_ENSLIG_FORSØRGER, directory, key, value)
    }

    override fun putTmp(directory: String, key: String, value: String) {
        writeString(BUCKET_ENSLIG_FORSØRGER_MELLOMLAGRING, directory, key, value)
    }

    override fun get(directory: String, key: String): String? {
        return readString(BUCKET_ENSLIG_FORSØRGER, directory, key)
    }

    override fun getTmp(directory: String, key: String): String? {
        return readString(BUCKET_ENSLIG_FORSØRGER_MELLOMLAGRING, directory, key)
    }

    override fun delete(directory: String, key: String) {
        deleteString(BUCKET_ENSLIG_FORSØRGER, directory, key)
    }

    override fun deleteTmp(directory: String, key: String) {
        deleteString(BUCKET_ENSLIG_FORSØRGER_MELLOMLAGRING, directory, key)
    }

    private fun ensureBucketExists(bucketName: String) {
        val bucketExists = s3.listBuckets().stream()
                .anyMatch { b -> b.name == bucketName }
        if (!bucketExists) {
            createBucket(bucketName)
        }
    }

    private fun ensureBucketExists(bucketName: String, expirationInDays: Int?) {
        ensureBucketExists(bucketName)
        s3.setBucketLifecycleConfiguration(bucketName, objectExpiresInDays(expirationInDays))
    }

    private fun createBucket(bucketName: String) {
        s3.createBucket(CreateBucketRequest(bucketName)
                                .withCannedAcl(CannedAccessControlList.Private))
    }

    private fun writeString(bucketName: String, directory: String, key: String, value: String) {
        s3.putObject(bucketName, fileName(directory, key), value)
    }

    private fun readString(bucketName: String, directory: String, key: String): String? {
        val path = fileName(directory, key)
        return try {
            val s3Object = s3.getObject(bucketName, path)
            BufferedReader(InputStreamReader(s3Object.objectContent))
                    .lineSequence()
                    .joinToString(separator = "\n")
        } catch (ex: AmazonS3Exception) {
            LOG.trace("Kunne ikke hente {}, finnes sannsynligvis ikke", path)
            null
        }

    }

    private fun deleteString(bucketName: String, directory: String, key: String) {
        s3.deleteObject(bucketName, fileName(directory, key))
    }

    override fun toString(): String {
        return javaClass.simpleName + " [s3=" + s3 + "]"
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(S3Storage::class.java)

        private const val BUCKET_ENSLIG_FORSØRGER = "ensligForsørgerSøknad"
        private const val BUCKET_ENSLIG_FORSØRGER_MELLOMLAGRING = "mellomlagring"

        private fun objectExpiresInDays(days: Int?): BucketLifecycleConfiguration {
            return BucketLifecycleConfiguration().withRules(
                    BucketLifecycleConfiguration.Rule()
                            .withId("soknad-retention-policy-" + days!!)
                            .withFilter(LifecycleFilter())
                            .withStatus(BucketLifecycleConfiguration.ENABLED)
                            .withExpirationInDays(days))
        }

        private fun fileName(directory: String, key: String): String {
            return directory + "_" + key
        }
    }
}
