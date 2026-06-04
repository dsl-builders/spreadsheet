#!/bin/sh

set -eu

if [ -z "${LARGE_SECRET_PASSPHRASE:-}" ]; then
  echo "LARGE_SECRET_PASSPHRASE must be set" >&2
  exit 1
fi

# --batch to prevent interactive command --yes to assume "yes" for questions
gpg --quiet --batch --yes --symmetric --cipher-algo AES256 \
  --passphrase="$LARGE_SECRET_PASSPHRASE" \
  --output libs/spreadsheet-builder-google/tokens/StoredCredential.gpg \
  libs/spreadsheet-builder-google/tokens/StoredCredential
