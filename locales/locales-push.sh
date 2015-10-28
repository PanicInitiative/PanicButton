#!/bin/bash
#git clone --depth=1 https://github.com/PanicInitiative/panicbutton.io

for res in "mobile" # "help"
do
  mkdir -p ./build/$res/en
  for f in panicbutton.io/_posts/$res/*.md
  do 
    echo "Processing $f file.."; 
    cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); data.slice(lio+1)' --outfmt text > ./build/$res/en/`basename $f`.txt
    echo "./build/$res/en/`basename $f`.txt file created."; 
    cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); var io =  data.indexOf("---"); if (lio != io) {data.slice(io+1,lio)} else {data.slice(0,lio)}' --outfmt text | yaml2json - | jq '.' > ./build/$res/en/`basename $f`.json.orig
    echo "./build/$res/en/`basename $f`.json.orig file created."; 
    cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); var io =  data.indexOf("---"); if (lio != io) {data.slice(io+1,lio)} else {data.slice(0,lio)}' --outfmt text | yaml2json - | jq --arg file `basename $f` -f pre-tx-push.jq > ./build/$res/en/`basename $f`.json.tmp
    echo "./build/$res/en/`basename $f`.json.tmp file created."; 
    jq '$json[0] * { ("content-" + $file): . }' --arg file `basename $f` --slurpfile json ./build/$res/en/`basename $f`.json.tmp -sR ./build/$res/en/`basename $f`.txt > ./build/$res/en/`basename $f`.json
    echo "./build/$res/en/`basename $f`.json file created."; 
  done

  jq 'add' -s build/$res/en/*.json > ./build/$res.en.json

  for lang in "es" "pt" "de" "fr"
  do
    mkdir -p ./build/$res/$lang

    for f in panicbutton.io/_posts/$res/$lang/*.md
    do 
      echo "Processing $f file.."; 
      cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); data.slice(lio+1)' --outfmt text > ./build/$res/$lang/`basename $f`.txt
      echo "./build/$res/$lang/`basename $f`.txt file created."; 
      cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); var io =  data.indexOf("---"); if (lio != io) {data.slice(io+1,lio)} else {data.slice(0,lio)}' --outfmt text | yaml2json - | jq '.' > ./build/$res/$lang/`basename $f`.json.orig
      echo "./build/$res/$lang/`basename $f`.json.orig file created."; 
      cat $f | underscore --infmt text process 'data' | underscore process 'var lio = data.lastIndexOf("---"); var io =  data.indexOf("---"); if (lio != io) {data.slice(io+1,lio)} else {data.slice(0,lio)}' --outfmt text | yaml2json - | jq --arg file `basename $f` -f pre-tx-push.jq > ./build/$res/$lang/`basename $f`.json.tmp
      echo "./build/$res/$lang/`basename $f`.json file created."; 
      jq '$json[0] * { ("content-" + $file): . }' --arg file `basename $f` --slurpfile json ./build/$res/$lang/`basename $f`.json.tmp -sR ./build/$res/$lang/`basename $f`.txt > ./build/$res/$lang/`basename $f`.json
      echo "./build/$res/$lang/`basename $f`.json file created."; 
    done

    jq 'add' -s build/$res/$lang/*.json > ./build/$res.$lang.json
  done
done