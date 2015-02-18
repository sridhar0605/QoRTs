# QoRTs: Quality Of Rna-seq Tool Set
Version 0.1.11 ([back to index](index.html))

([back to main](../index.html)) ([back to java-utility help](index.html))## Help for java command "bamToWiggle"

## USAGE:

    java [Java Options] -jar QoRTs.jar bamToWiggle [options] infile.bam[,infile2.bam,...] trackTitlePrefix chromLengthFile outfilePrefix


## DESCRIPTION:

Generates '\.wig' wiggle files, suitable for use with the UCSC genome browser or similar tools\. Wiggle files contain depth\-of\-coverage counts for all equally\-sized windows across the entire genome\. These depth\-of\-coverage counts can be either be by read \(the default\) or by read\-pair\.

## REQUIRED ARGUMENTS:
### infile.bam[,infile2.bam,...]:

> The input sam or bam file or files, or '-' to read from stdin. Note: if you include more than one bam file, the list must be comma delimited with no whitespace! (CommaDelimitedListOfStrings)


### trackTitlePrefix:

> The prefix for the title for the wiggle track. Track titles will be  (String)


### chromLengthFile:

> The chrom length file. This should be a simple tab-delimited text file that includes each chromosome in the first column and the chromosome's length (in base-pairs) in the second column. If the wiggle file is intended for use with a standard genome on the UCSC genome browser, then the UCSC utility "fetchChromSizes" should be used to generate this file, as the UCSC genome browser and related utilities may have difficulties if the chrom lengths do not match the expected values. (String)


### outfilePrefix:

> The output file prefix. If unstranded, this will produce one file named "outfilePrefix.unstranded.wig.gz". If stranded, this will produce two files: "outfilePrefix.fwd.wig.gz" and "outfilePrefix.rev.wig.gz". If the "--noGzipOutput" flag is raised then the output files will not have the ".gz" extension at the end. IMPORTANT NOTE: if the window size is set to any size other than the default of 100, the window size will be added to the filename. The filename will be  "outfilePrefix.win50.unstranded.wig.gz" for unstranded wiggles with a 50bp window, and so on. (String)



## OPTIONAL ARGUMENTS:
### --sizefactor float:

> The size factor, for normalization. Defaults to 1. If set, then the count of every cell will be divided by the given value. (Double)

### --coordSorted:

> Flag to indicate that input bam file is coordinate-sorted, rather than name-sorted. Note that QoRTs will take longer to run and use more memory in this mode. To improve performance, sort the data by name prior to using of QoRTs. In addition, if an (extremely) large fraction of the read-pairs are mapped to extremely distant loci (or different chromosomes), then memory issues may arise. However, this should not be a problem with most datasets. Technically this function will also allow QoRTs to work on unsorted bam files, but this is STRONGLY not recommended, as memory usage will by greatly increased. (flag)

### --minMAPQ num:

> Filter reads for the given minimum MAPQ. Set to 0 to turn off mapq filtering. (Int)

### --noGzipOutput:

> Flag to indicate that output should NOT be gzipped. (flag)

### --negativeReverseStrand:

> Flag to indicate that the reverse strand should be counted in negative values. Can be useful for plotting stranded data on a single multiwig track, via trackhubs. (flag)

### --countByReadPair:

> DEPRECIATED, nonfunctional. (flag)

### --simpleCountByRead:

> Flag to indicate that this utility should count 'read coverage' rather than 'read-pair coverage'. If this flag is raised, then regions where a read-pair overlaps will be counted TWICE, one for each read. By default each read is counted separately, so a read-pair where the reads overlap will be double-counted. By default, overlapping regions of read-pairs are counted only once. (flag)

### --testRun:

> Flag to indicate that only the first 100k reads should be read in, used for test runs. (flag)

### --stranded:

> The stranded flag, used for strand-specific data. If this is set, then two wiggle files are created, one for each strand. (flag)

### --stranded\_fr\_secondstrand:

> If this option is set, the data is assumed to be a fr\_secondstrand type stranded library. By default the assumed library type for stranded data is fr\_firststrand. (flag)

### --singleEnded:

> Flag for single-end data. Note that many other options do not apply in this case (for example: option --countPairsTogether does nothing in single-end mode) (flag)

### --keepMultiMapped:

> Flag to indicate that the tool should NOT filter out multi-mapped reads. Note that even with this flag raised this utility will still only use the 'primary' alignment location for each read. By default any reads that are marked as multi-mapped will be ignored entirely. (flag)

### --includeTrackDefLine:

> DEPRECIATED, nonfunctional (flag)

### --omitTrackDefLine:

> Flag to indicate that a track definition line should NOT be included. (flag)

### --additionalTrackOptions "track options":

> More options for the track. For more information refer to the wiggle track definition on the UCSC genome browser website. (String)

### --readGroup readGroupName:

> If this option is set, all analyses will be restricted to ONLY reads that are tagged with the given readGroupName (using an RG tag). This can be used if multiple read-groups have already been combined into a single bam file, but you want to summarize each read-group separately. (String)

### --rgbColor rval,gval,bval:

> A comma delimited list of integers from 0-255, indicating the rgb color and alt-color for the wiggle file.This is equivalent to using --additionalTrackOptions "color=r,g,b altColor=r,g,b itemRgb=On" (String)

### --noTruncate:

> The UCSC tool wigToBigWig only allows wiggle files in which every window is of equal size. This means that if the chromosome size is not divisible by the window size, a few bases are not counted on the end of the chromosome. Using this flag will cause this utility to not truncate off the last odd-sized window. However, be aware that this will mean that you cannot use the UCSC 'wigToBigWig' utility to convert the wiggle file to a (smaller and more efficient) bigWig file. (flag)

### --windowSize num:

> The length, in base-pairs, for each counting bin, or "window". Note: if this is set low the utility will take longer to run and will consume more memory. The default window size is 100bp. (Int)

### --verbose:

> Flag to indicate that debugging information and extra progress information should be sent to stderr. (flag)

### --quiet:

> Flag to indicate that only errors and warnings should be sent to stderr. (flag)

## AUTHORS:

Stephen W\. Hartley, Ph\.D\. <stephen\.hartley@nih\.gov>

## LEGAL:

 This software is "United States Government Work" under the terms of the United States Copyright  Act\.  It was written as part of the authors' official duties for the United States Government and  thus cannot be copyrighted\.  This software is freely available to the public for use without a  copyright notice\.  Restrictions cannot be placed on its present or future use\.  Although all reasonable efforts have been taken to ensure the accuracy and reliability of the  software and data, the National Human Genome Research Institute \(NHGRI\) and the U\.S\. Government  does not and cannot warrant the performance or results that may be obtained by using this software  or data\.  NHGRI and the U\.S\. Government disclaims all warranties as to performance, merchantability  or fitness for any particular purpose\.  In any work or product derived from this material, proper attribution of the authors as the source  of the software or data should be made, using "NHGRI Genome Technology Branch" as the citation\.  NOTE: This package includes \(internally\) the sam\-1\.113\.jar library from picard tools\. That package uses the MIT license, which can be accessed using the command:  java \-jar thisjarfile\.jar help samjdkinfo
