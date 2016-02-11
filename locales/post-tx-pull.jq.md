

def process_key(k): 
  if (.[k] | type == "string") then { ( k + "-" + $file ):.title } else {} end;

> that's a function 

def process_subkey(sk): 
  if (.[sk]) 
  
> if the key that is passed exists ```sk```
  
  then ( [ ( .[sk] | keys[] as $i 
  
> loop into the subkeys and call them $i

    | if (.[$i].title )
  
> if ```sk.title``` exists 
    
      then { key: ( sk 
                  + "-" 
                  + if (.[$i].link) 
                    then .[$i].link
                    elif (.[$i].status) 
                    then .[$i].status
                    else ( $i | tostring )
                    end 
                  + "-"
                  + $file )
                  
> here the name of the nested key is built {sk}-{sk.link.value || sk.status.value || sk.$i }-{filename.md}

           , value: .[$i].title }
           
> the value is always sk.$i.title

      else {}
      end 
    ) ] | from_entries ) 
    
> ```from_entries``` creates a { "mykey": "myvalue" } object from a { key: mykey, value: myvalue } object.   

  else {}
  end ;
  
> this function deals with nested objects. 
  

[ process_key("title")
> each element of this array is dealing with a particular key and the *add* at the end merges it back into an object.

, process_key("introduction")
, process_key("fail")
, process_subkey("action")

, process_subkey("checklist")
, process_subkey("status")
, process_subkey("items")
] | add
