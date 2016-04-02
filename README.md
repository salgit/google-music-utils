iTunesClone
==================

iTunesClone is a small utility to efficiently replicate an iTunes library into an alternate directory structure. The goal of the project is to create a sparse clone, transcoding files which are not supported in the "cloned" repo, while maintaining hard links for "supported" file types to minimize disk usage.

The original reason for creating this utility was to clone my iTunes library which is largely ALAC into an alternate directory strucutre which I could use to sync to Google Music. Since Google Music now natively supports ALAC, I stopped using it for that purpose. Currently I use it to replicate the library for my wife's Tesla which cannot read ALAC, but will handle FLAC.
