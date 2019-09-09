#!/bin/bash

grep "<w>" ../.idea/dictionaries/* | awk -F"<w>" '{print $2}' | awk -F"</w>" '{print $1}' >> ./ordbok.dic
sort -u ordbok.dic > temp.dic
mv temp.dic ordbok.dic
