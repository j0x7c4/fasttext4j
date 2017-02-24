#!/usr/bin/python
# -*- coding: utf-8 -*-
from optparse import OptionParser
import numpy as np
import sys

def run(op):
    with open(op.test_file) as f:
        test_list = [x.split(',', 1) for x in f]
    with open(op.predict_file) as f:
        predict_list = [x.split(' ', 1) for x in f]

    if len(test_list) == len(predict_list):
        with open(op.output_file, 'w') as f:
            for i in range(len(test_list)):
                origin_label = test_list[i][0].strip()
                content = test_list[i][1].strip()
                predict_label = predict_list[i][0].strip()
                predict_prob = float(predict_list[i][1].strip())
                f.write("%s,%s,%f,%s\n" % (origin_label, predict_label, predict_prob, content))
    else:
        print >> sys.stderr, "difference in records number"
        sys.exit(1)

def main():
    parser = OptionParser()
    parser.add_option("-t", "--input--test", dest="test_file")
    parser.add_option("-p", "--input--predict", dest="predict_file")
    parser.add_option("-o", "--output", dest="output_file")
    (options, args) = parser.parse_args()
    run(options)

if __name__ == "__main__":
    main()
