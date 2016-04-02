iTunesClone
==================

iTunesClone is a small utility to efficiently replicate an iTunes library into an alternate directory structure. The goal of the project is to create a sparse clone, transcoding files which are not supported in the "cloned" repo, while maintaining hard links for "supported" file types to minimize disk usage.

The original reason for creating this utility was to clone my iTunes library (largely ALAC) into an alternate directory structure which I could use to sync to Google Music. The tool hard linked all MP3, AAC files and transcoded ALAC to MP3. Since Google Music now natively supports ALAC, I stopped using it for that purpose. Currently I use it to replicate the library for my wife's car which cannot read ALAC, but will handle FLAC. So the tool now supports converting ALAC to FLAC and retains hard links for MP3, AAC etc.
