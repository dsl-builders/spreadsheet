#!/bin/sh

set -eu

if [ -z "${DSL_SPREADSHEET_BUILDER_GOOGLE_CLIENT_SECRET:-}" ]; then
  echo "DSL_SPREADSHEET_BUILDER_GOOGLE_CLIENT_SECRET must be set" >&2
  exit 1
fi

if [ -z "${LARGE_SECRET_PASSPHRASE:-}" ]; then
  echo "LARGE_SECRET_PASSPHRASE must be set" >&2
  exit 1
fi

rm -f libs/spreadsheet-builder-google/tokens/StoredCredential

./gradlew :spreadsheet-builder-google:test \
  --tests builders.dsl.spreadsheet.google.GoogleBuilderSpec \
  --no-daemon --console=plain

libs/spreadsheet-builder-google/tokens/encrypt_secret.sh
rm -f libs/spreadsheet-builder-google/tokens/StoredCredential

echo "Refreshed libs/spreadsheet-builder-google/tokens/StoredCredential.gpg"
