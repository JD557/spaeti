set windows-shell := ["powershell.exe", "-NoLogo", "-Command"]

build: format
  scala-cli --power compile --suppress-experimental-warning .

check-format:
  scalafmt . --check

format:
  scalafmt .

test:
  scala-cli --power test --suppress-experimental-warning .

run:
  scala-cli --power --suppress-experimental-warning .

publish:
  scala-cli --power publish .

clean:
  scala-cli clean .
