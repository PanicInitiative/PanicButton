# Helping with the Panic Button translation

## Submit a translation

Unfortunately it is not as straightforward as we would like right now, but if you have resources or skills to help make the process better, [that would be a great contribution](#improve-how-translations-are-managed) to the project.

In the meantime you can contribute by:
 - [forking](https://help.github.com/articles/fork-a-repo/) the project
 - creating a ```res/value-XX``` directory (where XX is the 2 letter code for the language) with a translated string.xml file (the english version is [here](https://github.com/iilab/PanicButton/blob/master/res/values-en/strings.xml)) 
 - creating ```help_XX.json``` and ```mobile_kh.json``` files in the [assets directory](https://github.com/iilab/PanicButton/tree/master/assets).
 - adding code to load the ```mobile_XX.json``` file [after this block](https://github.com/iilab/PanicButton/blob/master/src/main/java/org/iilab/pb/HomeActivity.java#L234-L242) and ```help_XX.json``` after [this block](https://github.com/iilab/PanicButton/blob/master/src/main/java/org/iilab/pb/HomeActivity.java#L294-L302)
 - [submitting a pull request](https://help.github.com/articles/using-pull-requests).

## Improve how translations are managed.

If you have software development skills then you can help us by streamlining how the translation process happens. One of the ideal outcomes would be to be able to manage translations via transifex. There were [some efforts towards this](https://github.com/iilab/PanicButton/issues/43). What complicates matters currently is that, as a product of our [previous API driven content loading approach](https://github.com/iilab/PanicButton/issues/40) there are other language files in the ```assets``` directory.  

Read more about this and let us know your ideas on the [dedicated github issue](https://github.com/iilab/PanicButton/issues/82)!
