# Changelog

## [0.3.0+26.1.2]

### Added
- Added a research reward system
  - Built-in reward types are `researcher:experience`, `researcher:fireworks`, and `researcher:loot`
  - Mod can make and register their own reward types
- Added research finished events (both client and server)
- You can now "highlight-lock" research nodes in the research tree view
  - This is useful for viewing connections in complex trees
  - You can "unlock" by clicking on the background
  - You will probably never use this

### Changed
- Adjusted some colors in the research tree view to improve visibility, especially against bright backgrounds
- The research list view can now be scrolled
- Scrolling in the research tree view is now limited properly
  - This took way too long
- Optimized png images
- JSON field names now use snake case instead of camel case
- Mess with data serialization some more
- Made more breaking changes probably

### Fixed
- Fixed issues in the research screen
  - Chances are there are still a few things remaining
- Other bugs probably

## [0.3.0-beta.1+26.1]

**Updated to 26.1**

### Added
- The researches needed to unlock a recipe are now shown in recipe viewers
  - This currently only works with JEI

### Changed
- Some things

### Fixed
- Many things

## [0.2.0-beta.1+1.21.11]

### Added
- Implemented a layered graph algorithm for the research tree view
  - The default tree mode is now ALL_RELATED
- Added the ALL research tree mode
  - This displays every research in the graph and is not recommended
- Added rudimentary support for RRV

### Changed
- Messed with the api
  - Uses a custom graph implementation now
- Updated to support Codec2Schema 0.2

## [0.1.1+1.21.11]

### Fixed
- Fixed key category text

## [0.1.0+1.21.11]

Initial release
