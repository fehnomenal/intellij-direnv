<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# intellij-direnv Changelog

## [Unreleased]

## [0.2.8] - 2023-08-15
- Version range now includes 2023.2

## [0.2.7] - 2023-04-10
- Version range now includes 2023.1

## [0.2.6] - 2022-12-31
- Remove 2020.2 & 2020.3 due to compatibility problems
- Version range now includes 2022.3
- Apply changes from [template v1.3.0](https://github.com/JetBrains/intellij-platform-plugin-template/releases/tag/v1.3.0)

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

[Unreleased]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.8...HEAD
[0.2.8]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.7...v0.2.8
[0.2.7]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.6...v0.2.7
[0.2.6]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.5...v0.2.6
[0.2.5]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.4...v0.2.5
[0.2.4]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.3...v0.2.4
[0.2.3]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.2...v0.2.3
[0.2.2]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.1...v0.2.2
[0.2.1]: https://github.com/fehnomenal/intellij-direnv/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/fehnomenal/intellij-direnv/compare/v0.1.3...v0.2.0
[0.1.3]: https://github.com/fehnomenal/intellij-direnv/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/fehnomenal/intellij-direnv/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/fehnomenal/intellij-direnv/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/fehnomenal/intellij-direnv/compare/v0.0.3...v0.1.0
[0.0.3]: https://github.com/fehnomenal/intellij-direnv/compare/v0.0.1...v0.0.3
[0.0.1]: https://github.com/fehnomenal/intellij-direnv/commits/v0.0.1
