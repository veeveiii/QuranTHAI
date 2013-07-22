qth_parser - Generic scripture data indexer for load in Android, j2me
==========

Generic scripture data indexer (for huge text in the UTF-8 text formatted with chapter:verse) - designed to make easy-load-to-mem possible for both j2me and android - this program cuts, does indexing and make key files to load the data.

Reading, loading, skipping in the the whole multi-megabyte file is often impossible/fails on mobile phones with low memory, so the purpost of this project is to make it possible to jump and read any chapter:verse from the huge original text file. Sample data is from a Thai Quran translation.

NOTE: The quality/standard of the code is quite low as this was written when I had less experience in programming, but it continued to work so there was no need to rewrite it.

Howto
----

Generally, we take a huge text file containing lines of "verses" in the format like the sample "th.txt" that qth_parser sucessfully parses and generates the "data" folder with all the files, then we put that "data" folder into the reader application - [QuranTHAI sample reader project](https://github.com/ykasidit/QuranTHAI) - into its "assets" folder. Then we use/customize that reader application to read (mainly in VerseLoader.java) and display that "data".

To build manually with apache ant in this base folder and run:
ant

To run with commandline param telling the input file:
java qth_parser th.txt

Input text file format
----------------------

(empty line)
(chapter_num):(verse_num)(single_space)(verse_contents)
...

The sample th.txt and ar.txt were generated from original files from www.qurandatabase.org - used Gnumeric to edit them and save into plain text_configurable and in its wizard/form - use "unix line feed", custom > (empty) separator, quoting > never, encoding: utf-8.

Credits to other projects
------------------------

Credit to http://www.qurandatabase.org/ for providing both the Arabic text and Thai translations used here in an easy-to-program format.

