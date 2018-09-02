#!/usr/bin/python

import os
commit = raw_input("Commit name: ")
branch = raw_input("Branch: ")
os.system(" git commit -m " + commit)
os.system(" git pull origin " + branch)
os.system(" git push origin  " + branch)
