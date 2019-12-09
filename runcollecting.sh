#!/usr/bin/env bash

abs_dirname() {
  local cwd="$(pwd)"
  local path="$1"

  while [ -n "$path" ]; do
    cd "${path%/*}"
    local name="${path##*/}"
    path="$(readlink "$name" || true)"
  done

  pwd -P
  cd "$cwd"
}

# move to the repository root
script_dir="$(abs_dirname "$0")"
cd ${script_dir}

# execute main script
./gradlew --quiet installDist
./build/install/image-auto-collecting/bin/image-auto-collecting "$@"
