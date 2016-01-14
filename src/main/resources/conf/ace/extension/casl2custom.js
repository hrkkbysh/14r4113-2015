    var editor = ace.edit("editor");
    var langTools = ace.require("ace/ext/language_tools");
    editor.setTheme("ace/theme/twilight");
    editor.session.setMode("ace/mode/casl2");

    editor.setScrollSpeed(0.05);
    document.getElementById('editor').style.fontSize='20px';
    /* javafxからの呼び出し用。 */
    function pasteContent(content){
        editor.onPaste(content);
    }
    editor.setOptions({
    enableBasicAutocompletion: true,
    enableSnippets: true,
    enableLiveAutocompletion: true,
    showPrintMargin: false,
    showInvisibles: true
    });