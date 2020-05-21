#!/usr/bin/env bash

set -eu

is_pull_request_build() {
    if [ "${GH_EVENT_NAME}" == "pull_request" ]; then
        echo "✅ Build for pull request ${GH_EVENT_NAME}"
        return 0
    else
        echo "🚫 Not a pull request"
        return 1
    fi
}

is_branch_master() {
    if [ "${GH_REF}" == "master" ]; then
        echo "✅ Branch is master"
        return 0
    else
        echo "🚫 Branch ${GH_REF} is not master"
        return 1
    fi
}

is_feature_branch_version() {
    regex='^[0-9]+(\.[0-9]+)+(-[[:alnum:].]+)+$'
    if [[ ${version} =~ $regex ]]; then
        echo "✅ Version ${version} is a feature branch version"
        return 0
    else
        echo "🚫 Version ${version} is not a feature branch version"
        return 1
    fi
}

function is_publishable() {
    if ! is_pull_request_build && (is_branch_master || is_feature_branch_version); then
        echo "✅ Version ${version} can be published"
        return 0
    else
        echo "🚫 Version ${version} cannot be published"
        return 1
    fi
}

function is_releasable() {
    if ! is_pull_request_build && is_branch_master; then
        echo "✅ Version ${version} can be released"
        return 0
    else
        echo "🚫 Version ${version} cannot be released"
        return 1
    fi
}

readonly version=$(docker run -i denisa/clq -release -query 'releases[0].version' < CHANGELOG.md)
readonly tag=v${version}

if is_branch_master || is_pull_request_build; then
    # shellcheck disable=SC2154
    if git ls-remote -q --tags | grep -q "${tag}$"; then
        # shellcheck disable=SC2154
        echo "🚫 Tag ${tag} already exists for version ${version}"
        echo "   Update from master and update the CHANGELOG.md"
        exit 1
    fi
    echo "✅ version ${version} hasn't been used yet"
else
    echo "✅ uniqueness of version ${version} isn't validated on a branch build"
fi

if is_publishable; then
    mvn -B --no-transfer-progress -Drevision="${version}" -Pci,releaseValidation install
else
    mvn -B --no-transfer-progress -Drevision="${version}" verify
fi

if is_releasable; then
    git config --local user.name "github-ci"
    git config --local user.email "github-ci@anvil-software.com"
    # shellcheck disable=SC2154
    git tag "${tag}" -m "Release ${tag}"
    git push origin "${tag}"
fi
