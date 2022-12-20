<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# intellij-direnv Changelog

## [Unreleased]
- Version range now includes 2022.3
- Apply `build.gradle.kts` changes from template v1.3.0

## [0.2.5]
### Added
- New settings screen to customize the path to the direnv executable

### Changed
- Version range now includes 2022.2

## [0.2.4]
### Changed
- Make compatible with 2022.1

## [0.2.3]
### Changed
- Make compatible with 2021.3.1

## [0.2.2]
### Changed
- Make compatible with 2021.2
- Apply changes from template v0.10.1

## [0.2.1]
### Changed
- Make compatible with 2021.1

## [0.2.0]
### Added
- Allow importing non-toplevel `.envrc` files

### Changed
- Toolbar action is always visible and enabled

## [0.1.3]
### Changed
- Remove 2019.3 due to compatibility problems

## [0.1.2]
### Changed
- Version range now includes 2020.4

## [0.1.1]
### Changed
- The `direnv` executable is no longer looked up in `PATH`. Instead, Java is responsible for this.

## [0.1.0]
### Added
- On project load a popup suggests importing from direnv
- Document that only top-level `.envrc` files are handled
- Provide more feedback over the direnv import

### Changed
- Action is only available if `direnv` is in path, and the project contains a `.envrc` file

### Fixed
- Changed environment is propagated correctly to direnv

## [0.0.3]
- Add the official icon and action icon

## [0.0.1] - 2020-10-27
- Initial release
