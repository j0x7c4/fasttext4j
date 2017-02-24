#!/usr/bin/env bash
#
# Copyright (c) 2016-present, Facebook, Inc.
# All rights reserved.
#
# This source code is licensed under the BSD-style license found in the
# LICENSE file in the root directory of this source tree. An additional grant
# of patent rights can be found in the PATENTS file in the same directory.
#

myshuf() {
  perl -MList::Util=shuffle -e 'print shuffle(<>);' "$@";
}

normalize_text() {
  tr '[:upper:]' '[:lower:]' | sed -e 's/^/__label__/g' | \
    sed -e "s/'/ ' /g" -e 's/"//g' -e 's/\./ \. /g' -e 's/<br \/>/ /g' \
        -e 's/,/ , /g' -e 's/(/ ( /g' -e 's/)/ ) /g' -e 's/\!/ \! /g' \
        -e 's/\?/ \? /g' -e 's/\;/ /g' -e 's/\:/ /g' | tr -s " " | myshuf
}

RESULTDIR=result
DATADIR=data

mkdir -p "${RESULTDIR}"
mkdir -p "${DATADIR}"

if [ ! -f "${DATADIR}/note.train" ]
then
  cat "${DATADIR}/note_classify_train_set.csv" | normalize_text > "${DATADIR}/note.train"
  cat "${DATADIR}/note_classify_test_set.csv" | normalize_text > "${DATADIR}/note.test"
fi

./fasttext supervised -input "${DATADIR}/note.train" -output "${RESULTDIR}/note" -dim 15 -lr 0.001 -wordNgrams 5 -minCount 3 -bucket 10000000 -epoch 1000 -thread 4

./fasttext test "${RESULTDIR}/note.bin" "${DATADIR}/note.test"

./fasttext predict-prob "${RESULTDIR}/note.bin" "${DATADIR}/note.test" > "${RESULTDIR}/note.test.predict"

python diff.py -t "${DATADIR}/note.test" -p "${RESULTDIR}/note.test.predict" -o "${RESULTDIR}/note.test.predict.diff"