ace.define("ace/mode/casl2_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var CASL2HighlightRules = function() {

    this.$rules = {
        "start" : [
            {
                token: 'keyword.機械語命令',
                regex: '\\b(?:ADDA|ADDL|AND|CALL|CPA|CPL|JMI|JNZ|JOV|JPL|JUMP|JZE|LAD|LD|NOP|OR|OUT|POP|PUSH|RET|SLA|SLL|SRA|SRL|ST|SUBA|SUBL|SVC|XOR)\\b',
                caseInsensitive: true
            },
            {
                token: 'keyword.アセンブラ命令',
                regex: '\\b(?:DC|DS|END|START)\\b',
                caseInsensitive: true
            },
            {
                token: 'keyword.マクロ命令',
                regex: '\\b(?:IN|OUT|RPOP|RPUSH)\\b',
                caseInsensitive: true
            },
            {
                token: 'support.other',
                regex: '=',
                caseInsensitive: true
            },
            { 
                token: 'variable.parameter.register.assembly',
                regex: '\\b(?:GR[0-7]|PR|SP)\\b',
                caseInsensitive: true 
            },
            { token: 'constant.character.decimal.assembly',
           regex: '\\b[0-9]+\\b' },
           { token: 'constant.character.hexadecimal.assembly',
           regex: '#[A-F0-9]+\\b',
           caseInsensitive: true },
           { token:  'support.function.directive.assembly',
           regex: '^MACRO\\b'},
         { token: 'support.function.directive.assembly',
           regex: '^MEND' },
           { token: 'variable.assembly',
           regex: '\\$[\\w.]+?\\b' },
         { token: 'string.assembly', regex: /'([^\\']|\\.)*'/ },
         { token: 'entity.name.function.assembly', regex: '[\\w.]+?\\b' },
          { token: 'comment.assembly', regex: ';.*$' }
        ]
    };
    
    this.normalizeRules();
};

CASL2HighlightRules.metaData = { fileTypes: [ 'asm' ],
      name: 'CASL2',
      scopeName: 'source.casl2' }


oop.inherits(CASL2HighlightRules, TextHighlightRules);

exports.CASL2HighlightRules = CASL2HighlightRules;
});

ace.define("ace/mode/folding/coffee",["require","exports","module","ace/lib/oop","ace/mode/folding/fold_mode","ace/range"], function(require, exports, module) {
"use strict";

var oop = require("../../lib/oop");
var BaseFoldMode = require("./fold_mode").FoldMode;
var Range = require("../../range").Range;

var FoldMode = exports.FoldMode = function() {};
oop.inherits(FoldMode, BaseFoldMode);

(function() {

    this.getFoldWidgetRange = function(session, foldStyle, row) {
        var range = this.indentationBlock(session, row);
        if (range)
            return range;

        var re = /\S/;
        var line = session.getLine(row);
        var startLevel = line.search(re);
        if (startLevel == -1 || line[startLevel] != "#")
            return;

        var startColumn = line.length;
        var maxRow = session.getLength();
        var startRow = row;
        var endRow = row;

        while (++row < maxRow) {
            line = session.getLine(row);
            var level = line.search(re);

            if (level == -1)
                continue;

            if (line[level] != "#")
                break;

            endRow = row;
        }

        if (endRow > startRow) {
            var endColumn = session.getLine(endRow).length;
            return new Range(startRow, startColumn, endRow, endColumn);
        }
    };
    this.getFoldWidget = function(session, foldStyle, row) {
        var line = session.getLine(row);
        var indent = line.search(/\S/);
        var next = session.getLine(row + 1);
        var prev = session.getLine(row - 1);
        var prevIndent = prev.search(/\S/);
        var nextIndent = next.search(/\S/);

        if (indent == -1) {
            session.foldWidgets[row - 1] = prevIndent!= -1 && prevIndent < nextIndent ? "start" : "";
            return "";
        }
        if (prevIndent == -1) {
            if (indent == nextIndent && line[indent] == "#" && next[indent] == "#") {
                session.foldWidgets[row - 1] = "";
                session.foldWidgets[row + 1] = "";
                return "start";
            }
        } else if (prevIndent == indent && line[indent] == "#" && prev[indent] == "#") {
            if (session.getLine(row - 2).search(/\S/) == -1) {
                session.foldWidgets[row - 1] = "start";
                session.foldWidgets[row + 1] = "";
                return "";
            }
        }

        if (prevIndent!= -1 && prevIndent < indent)
            session.foldWidgets[row - 1] = "start";
        else
            session.foldWidgets[row - 1] = "";

        if (indent < nextIndent)
            return "start";
        else
            return "";
    };

}).call(FoldMode.prototype);

});

ace.define("ace/mode/casl2",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/casl2_highlight_rules","ace/mode/folding/coffee"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var CASL2HighlightRules = require("./casl2_highlight_rules").CASL2HighlightRules;
var FoldMode = require("./folding/coffee").FoldMode;

var Mode = function() {
    this.HighlightRules = CASL2HighlightRules;
    this.foldingRules = new FoldMode();
};
oop.inherits(Mode, TextMode);

(function() {
    this.lineCommentStart = ";";
    this.$id = "ace/mode/casl2";
}).call(Mode.prototype);

exports.Mode = Mode;
});
