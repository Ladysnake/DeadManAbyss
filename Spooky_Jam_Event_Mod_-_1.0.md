## Spooky Jam Event Mod - 1.0

### Main properties:

- Two ways the event can start:
  - Randomly after the player has been to the nether
  - dropping a black crystal lotus into water
- The event only concerns the overworld; Nether and End stay the same and aren't changed. (configurable)
- Normal mobs don't spawn, although they won't burn during the event.
- Sound system, mobs rely on sound to track the player. The more the player makes sound, the more their radius extend (for the duration of the noise).
- Shader during the event that makes it so dark areas are pitch black.
- Custom background ambience.
- Exiting the event will require actively passing time in it (by gathering artifacts?)

### Entities:

- Blind:
  - What appears to be what was once human is now just a walking living carcass devoted of sight and will. Some rumors say they were once humans, that decided to offer their eyes to a Banshee in order for her to spare them.
  - Mainly similar to zombies.
  - Doesn't burn in sunlight.
  - Will spawn in groups during the event, can spawn very rarely (1% when a zombie spawns) outside of it.
  - Drops Blind leather (like zombies would drop flesh).
  - <low priority> Head doesn't look at the player.
  - <low priority> When spectated, the spectator gets a black screen.

- Banshee:
  - Somewhat humanoid hunters that come out during the darkest of nights, they compensate their poor vision with an excellent hearing. But despite the fact they cannot see well, they notice light and will try to destroy any light source they can see.
  - Spawns during the event (alone).
  - Three possible stances:
    - Four legged: Speed stance (as fast as an enderman), used by default
    - High: Used when being close to a prey or attacking (1.5x slower than four legged stance)
    - <low priority> Clinging: When spawning in a cave, the Banshee will cling to the ceiling by default and wait until a player gets in her radius.
  - Attracted by sound and light sources (if the player stands still with no lightsource around him, the banshee will ignore him).
  - Will destroy any light source (torch, glowstone block, etc...) if she comes close enough to it.
  - Can be detected by their screams. The louder the scream, the closer they are.
  - High health (60hp), no armor.
  - <low priority> If a Banshee is on fire, and other Banshees are close, the Banshee on fire will get attacked.
  - <nothing else to do priority> Make biome specific Banshees, with specific ways of luring and defeating