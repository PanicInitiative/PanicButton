#!/bin/bash
#git clone --depth=1 https://github.com/PanicInitiative/panicbutton.io
#rm -rf ./build
#mkdir ./build
for f in panicbutton.io/_posts/mobile/*.md
do 
  echo "Processing $f file.."; 
  cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); data.slice(lio+1)' --outfmt text > ./build/`basename $f`.txt
  echo "./build/`basename $f`.txt file created."; 
  cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); var io =  data.indexOf("---"); if (lio != io) {data.slice(io+1,lio)} else {data.slice(0,lio)}' --outfmt text | yaml2json - | jq '.' > ./build/`basename $f`.json.orig
  echo "./build/`basename $f`.json.orig file created."; 
  cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); var io =  data.indexOf("---"); if (lio != io) {data.slice(io+1,lio)} else {data.slice(0,lio)}' --outfmt text | yaml2json - | jq --arg file `basename $f` -f pre-tx-push.jq > ./build/`basename $f`.json
  echo "./build/`basename $f`.json file created."; 
done

