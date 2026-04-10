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
