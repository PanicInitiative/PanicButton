def process_key(f): ( [ ( .[f] | keys[] as $i 
    | if (.[$i].title ) 
      then { key: ( f 
                  + "-" 
                  + if (.[$i].link) 
                    then .[$i].link
                    elif (.[$i].status) 
                    then .[$i].status
                    else ( $i | tostring )
                    end 
                  + "-"
                  + $file )
           , value: .[$i].title } 
      else {}
      end 
    ) ] | from_entries ) ;
[ if (.title) then { (.title|ascii_downcase):.title } else {} end
, if (.introduction) then { (.introduction|ascii_downcase): .introduction } else {} end
, if (.fail) then { (.fail|ascii_downcase): .fail } else {} end
, if (.action) then process_key("action") else {} end
, if (.checklist) then process_key("checklist") else {} end
, if (.status) then process_key("status") else {} end
, if (.items) then process_key("items") else {} end
] | add