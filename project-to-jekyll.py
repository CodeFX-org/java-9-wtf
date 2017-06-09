#!/usr/bin/python
import glob
import os
import re

root_targetdir = './gh-pages/_pages'
project_targetdir = './gh-pages/_posts'

def clean_dir(dir):
	"""Deletes all files in the specified directory"""
	files = glob.glob(dir + '/*')
	for f in files:
		os.remove(f)

def create_site():
	"""Creates home page and all posts from the individual project's folders"""
	for subdir in [ './' ] + glob.glob('./*/'):
		print('Processing ' + subdir)
		if os.path.isfile(subdir + 'meta.yaml') and os.path.isfile(subdir + 'README.md'):
			# drop the trailing slash for method calls
			if subdir == './':
				create_homepage(subdir[:-1], root_targetdir)
			else:
				create_post(subdir[:-1], project_targetdir)
		else:
			print_dir_abort('no meta.yaml and/or README.md found')

def create_homepage(sourcedir, targetdir):
	"""Creates the homepage from the specified source directory in
		the specified target directory"""
	# date can be ignored because pages don't need it
	(_, meta) = read_date_and_meta(sourcedir)
	content = read_readme(sourcedir)
	write_meta_and_content(meta, content, targetdir, "home.md")
	print_dir_success('written page file')

def write_meta_and_content(meta, content, targetdir, filename):
	"""Creates a Jekyll conformant file in the specified target directory
		and file with the specified meta and content"""
	file = open(targetdir + '/' + filename, mode = 'w', encoding = 'UTF-8')
	file.write('---\n')
	file.write(meta)
	file.write('---\n\n')
	file.write(content)
	file.close()

def create_post(sourcedir, targetdir):
	"""Creates a single post from the specified source directory in
		the specified target directory"""
	(date, meta) = read_date_and_meta(sourcedir)
	# if no date could be determined, the post can not be created
	if date is None:
		print_dir_abort('no date determined')
		return
	content = read_readme(sourcedir)
	write_post(sourcedir, date, meta, content, targetdir)
	print_dir_success('written post file')

def write_post(sourcedir, date, meta, content, targetdir):
	"""Creates a Jekyll conformant file in the specified target directory
		with the name determined by date and source directory and the content
 		by the given meta and content"""
	filename = date + '-' + sourcedir[2:] + '.md'
	print_dir_message('target file name: ' + filename)
	write_meta_and_content(meta, content, targetdir, filename)

def read_date_and_meta(sourcedir):
	"""Reads the meta.yaml in the specified directory and extracts the date
		as well as the rest of the file content"""
	metafile = open(sourcedir + '/meta.yaml', encoding = 'UTF-8')
	date = None
	meta = ''
	for line in metafile:
		meta += line
		if line.startswith('date:'):
			date = re.sub('^date:', '', line).strip()
			print_dir_message('determined date ' + date)
	if not meta.endswith('\n'):
		meta += '\n'
	return (date, meta)

def read_readme(sourcedir):
	"""Reads the README.md in the specified directory and removes all
		h1 headers because they would conflict with the title Jekyll sets"""
	readmefile = open(sourcedir + '/README.md', encoding = 'UTF-8')
	content = ''
	for line in readmefile:
		if not line.startswith('# '):
			content += line
	return content

def print_dir_abort(message):
	print_dir_message(message)
	print(' -> skipping directory\n')

def print_dir_success(message):
	print_dir_message(message)
	print(' -> done!\n')

def print_dir_message(message):
	print(' :: ' + message)


# ---------- #
# HERE WE GO #
# ---------- #

clean_dir(root_targetdir)
clean_dir(project_targetdir)
create_site()
