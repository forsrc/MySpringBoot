var term,
    protocol,
    socketURL,
    socket,
    pid;

var terminalContainer = document.getElementById('terminal-container');

createTerminal();

function createTerminal() {
  // Clean terminal
  while (terminalContainer.children.length) {
    terminalContainer.removeChild(terminalContainer.children[0]);
  }
  term = new Terminal({
    cursorBlink: true,
    scrollback: 10,
    tabStopWidth: 10
  });

  protocol = (location.protocol === 'https:') ? 'wss://' : 'ws://';
  socketURL = protocol + location.hostname + ((location.port) ? (':' + location.port) : '') + '/ws-myhtmlshell';

  term.open(terminalContainer);
  term.fit();

  var initialGeometry = term.proposeGeometry();


  fetch('/myhtmlshell/open', {method: 'POST'}).then(function (res) {
    res.text().then(function (pid) {
      window.pid = pid;
      //socketURL += pid;
      socket = new WebSocket(socketURL);
      socket.onopen = onopen;
      socket.onclose = runFakeTerminal;
      socket.onerror = runFakeTerminal;
    });
  });
}

function onopen() {
  term.attach(socket);
  term._initialized = true;
}

function runFakeTerminal() {
  if (term._initialized) {
    return;
  }

  term._initialized = true;

  var shellprompt = '$ ';

  term.prompt = function () {
    term.write('\r\n' + shellprompt);
  };

  term.writeln('Welcome to MyHtmlShell');
  term.writeln('');
  term.prompt();

  term.on('key', function (key, ev) {
    var printable = (
      !ev.altKey && !ev.altGraphKey && !ev.ctrlKey && !ev.metaKey
    );

    if (ev.keyCode == 13) {
      term.prompt();
    } else if (ev.keyCode == 8) {
     // Do not delete the prompt
      if (term.x > 2) {
        term.write('\b \b');
      }
    } else if (printable) {
      term.write(key);
    }
  });

  term.on('paste', function (data, ev) {
    term.write(data);
  });
}
