# Tools

These are tools related to the build process.

## Install
 - [jq](https://stedolan.github.io/jq/download/)
 - [nodejs](https://nodejs.org/en/download/)
 - underscore-cli ```npm install -g underscore-cli```
 - transifex client ```pip install transifex-client```

## Initialise

```
git clone --depth=1 https://github.com/PanicInitiative/panicbutton.io
./locales-push.sh
tx init
tx set --auto-local --type KEYVALUEJSON -r panicbutton.mobile 'mobile.<lang>.json' --source-lang en --execute
```

