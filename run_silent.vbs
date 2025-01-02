Set WshShell = CreateObject("WScript.Shell")
WshShell.Run "java -jar """ & WScript.Arguments(0) & """", 0, False
