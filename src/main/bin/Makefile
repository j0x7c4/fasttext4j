#
# Copyright (c) 2016-present, Facebook, Inc.
# All rights reserved.
#
# This source code is licensed under the BSD-style license found in the
# LICENSE file in the root directory of this source tree. An additional grant
# of patent rights can be found in the PATENTS file in the same directory.
#

CXX = c++
CXXFLAGS = -pthread -std=c++0x
OBJS = args.o dictionary.o matrix.o vector.o model.o utils.o fasttext.o
INCLUDES = -I.

opt: CXXFLAGS += -O3 -funroll-loops
opt: fasttext

debug: CXXFLAGS += -g -O0 -fno-inline
debug: fasttext

args.o: cpp/args.cc cpp/args.h
	$(CXX) $(CXXFLAGS) -c cpp/args.cc

dictionary.o: cpp/dictionary.cc cpp/dictionary.h cpp/args.h
	$(CXX) $(CXXFLAGS) -c cpp/dictionary.cc

matrix.o: cpp/matrix.cc cpp/matrix.h cpp/utils.h
	$(CXX) $(CXXFLAGS) -c cpp/matrix.cc

vector.o: cpp/vector.cc cpp/vector.h cpp/utils.h
	$(CXX) $(CXXFLAGS) -c cpp/vector.cc

model.o: cpp/model.cc cpp/model.h cpp/args.h
	$(CXX) $(CXXFLAGS) -c cpp/model.cc

utils.o: cpp/utils.cc cpp/utils.h
	$(CXX) $(CXXFLAGS) -c cpp/utils.cc

fasttext.o: cpp/fasttext.cc cpp/*.h
	$(CXX) $(CXXFLAGS) -c cpp/fasttext.cc

fasttext: $(OBJS) cpp/fasttext.cc
	$(CXX) $(CXXFLAGS) $(OBJS) cpp/main.cc -o fasttext

clean:
	rm -rf *.o fasttext
