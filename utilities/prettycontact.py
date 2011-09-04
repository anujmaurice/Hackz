# used for pretty printing the contacts backed up from samsung mobile [vcf format]
#2nd Sept 2011
import os
import time
path_to_contact = '/home/anuj/backup/contacts_1_9_2011/'
files = os.listdir(path_to_contact)
files.sort()
excpt = []
out ="%s \n"%(time.ctime())
for file in files:
	if file.split('.')[-1] =='vcf':
		try:
			lines = open(path_to_contact+file).readlines()
			out += "%s  : %s\n"%(lines[2].split(';')[3].replace('=20',' '),lines[3].split(';')[-1].split(':')[1].strip())
		except:
			excpt.append(file)

if excpt:
	print excpt

output = open('contact.txt','w')
output.write(out)
output.close()
