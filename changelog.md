v2.0.4
------
* Add JEI support
* Add Enchantment Descriptions support
* Fix error when placing certain block entities
* Fix statistic recording for runescribing and spellcrafting
* Allow the spellcasting altar to face different directions
* Fix bug with hoppering fuel into empty calcinators
* Fix additional block entity saving bugs
* Increment to Forge version 39.0.59
* Update recipes to use new Forge tags

v2.0.3
------
* Update model for runecarving table
* Fix misaligned mana cost widgets on Arcane Workbench screen
* Fix broken Spirit Lantern recipe
* Fix block entity saving

v2.0.2
------
* Update model for ritual altar
* Update texture for rock salt ore
* Add config file entries to control shrine spacing in worldgen
* Fix client crash when fetching affinities for items with circular recipe chains
* Fix client crash when fetching affinities for items made in a non-standard crafting grid
* Increment to Forge version 39.0.17

v2.0.1
------
* Full release for MC 1.18.1!

v2.0.0-beta
-----------
* Update to MC 1.18.1

v1.0.0
------
* Full release for MC 1.17.1!

v0.1.2-beta
-----------
* Increment to Forge version 37.1.0

v0.1.1-beta
-----------
* Return unscanned items to player when closing Analysis Table GUI

v0.1.0-beta
-----------
* Rename mod to Primal Magick
* Add update check URL

v0.0.22-alpha
-------------
* Add Dissolution Chamber, for ore tripling at the cost of Earth mana
* Fix multiplayer client crash when using Sanguine Crucible
* Increment to Forge version 37.0.104

v0.0.21-alpha
-------------
* Fix Z-ordering issues with Research Table tooltips
* Add Hoe of the Nurturing Sun, to stimulate rapid plant growth
* Add new research project using a Dragon Head as a research aid
* Increase reward for Draconic Energies research project
* Spell projectiles are no longer slowed by water
* Rune enchantments discovered from primal tools are now indexed upon discovery, rather than creation
* Add Conjure Light spell payload, to create temporary glow fields
* Update model for Spellcrafting Altar
* Allow glow and consecration fields to work underwater
* Add Conjure Stone spell payload, for when you just need a block of stone right now
* Changed default name of Earth Damage spells to "Earthen" from "Stone"
* Increment to Forge version 37.0.103

v0.0.20-alpha
-------------
* Add scan research identifying most research aids
* Add recipe book functionality to the concocter
* Add Essence Transmuter, to convert one source of essence into another
* Add Mystical Relics, fragments of which drop from certain mobs, to study for theories
* Increment to Forge version 37.0.97

v0.0.19-alpha
-------------
* Allow Heartwood to rarely drop from Sunwood or Moonwood trees
* Add Blood-Scrawled Ravings, a means to unlock the Blood source for the faint of heart
* Certain advanced research project materials are no longer selected until having been crafted a few times
* Certain research project materials now grant bonus theory progress
* Increase distance before companions will attempt to teleport to their owners
* Tighten target selection zone for companion teleportation
* Add Magic Protection enchantment, reducing damage taken from all magical sources
* Change rune enchantment recipe for Blast Protection
* Add Dowsing Rod, to provide feedback on ritual altar layouts
* Add scan research for the Inner Demon
* Increment to Forge version 37.0.84

v0.0.18-alpha
-------------
* Clarified research requirement text for Polymorph spell payload
* Allow players to eat Ambrosia even while full
* Fix Unbreaking enchant interaction with Earthshatter Hammer
* Made ritual warning messages more descriptive about missing offerings and props
* Fix bug reseting arcane recipe book data on player death or return from the End
* Added workaround for translucent textures not rendering under Fabulous graphics settings
* The Hellish Chain ability now only triggers from attacks that actually do damage
* Add extra audio-visual effects to active ritual altars
* The Ritual Altar now requires two empty blocks above it to start a ritual
* Fix clumping of mana cost widgets on arcane workbench screen
* Increased affinities of glowstone blocks
* Add storage block for Refined Salt
* Allow the Trade research project to accept any color of wool
* Grant affinities to music discs
* Increased drop rate of the Essence Thief enchantment
* Allow the Earthshatter Hammer to break down stone
* Attack spells now do half damage against other players
* Greatly increased penetration of Burst spells at all Power ranks
* Increment to Forge version 37.0.78

v0.0.17-alpha
-------------
* Allow Earthshatter Hammers to be repaired with iron ingots
* Make attunement gain and loss messages more descriptive
* Allow the Research Table to detect non-consumed material blocks within five blocks of itself
* Fix third-person appearance of several tools
* Add recipe book functionality to the arcane workbench
* Increment to Forge version 37.0.75

v0.0.16-alpha
-------------
* Fix bug preventing blocks broken by Break spells from dropping experience
* Wand transforms now require a two-second channel
* Fix statistic increment when quick crafting in the arcane workbench
* Made statistic names for discipline crafting more consistent with research requirements
* Allow librarians and wandering traders to sell items that grant arcane knowledge
* Fix behavior of owned tile entities while their owners are offline
* Reduce string requirements for enchanted cloth
* Add Ignyx, an unstable super-coal

v0.0.15-alpha
-------------
* Fix intermittent server crash

v0.0.14-alpha
-------------
* Fix caching bug in owned tile entities, ritual altars, and spell mines
* Require individual rune research to unlock empowered rune enchantments
* Replaced sugar cane's sky affinity with an equal amount of sea affinity
* Prevent empowered rune enchantment books from being offered for trade by villagers
* Allow runecarving tables to hold their inventory between uses
* Better describe the raw destructive power of the Disintegration enchantment
* Fix Treasure enchantment for mob loot when using spells
* Allow shift-clicking fuel items into calcinator input slot if fuel slot is full
* Fix rounding issue in wand mana calculations
* Increase bone block affinities
* Fix bug causing mod progress reset when returning from the End
* Fix bug in Arcanometer rendering of multi-part entities

v0.0.13-alpha
-------------
* Endermen can now be harmed by damaging touch-range spells
* Witches now have substantial resistance to damaging spells
* Fix bug where certain blocks could forget their owners in certain multiplayer situations
* Fix bug where the Spellcrafting Altar could consume double mana
* Fix bug where the Arcane Workbench could consume double mana
* Overhealing with the Healing spell payload now grants an absorb shield
* Allow interacting with Wand and Auto-Chargers via their central gap
* Made cloth armor prevent players from freezing in powder snow

v0.0.12-alpha
-------------
* Allow clearing wand spells using the Wand Inscription Table
* Add Auto-Charger, to automatically charge wands from nearby fonts
* Philosophy research for Earth and Sky now requires the player to be in the Overworld
* Add Dream Vision Talisman, a means to convert experience into observations
* Increased spell cast cooldown to 1.5sec, from one second
* Increased effectiveness of Quicken spell mod to 0.25sec per level, from 0.15sec per level
* Added research/grant_parents and research/grant_all debug commands
* Condense recipe index entries with the same name
* Allow clicking through to recipe pages from ingredient widgets
* Attunement meters now have a helpful tooltip
* Disable hopper interaction with Essence Furnaces; they're too primitive!
* Fix hopper interaction with various blocks
* Rebalance calcinator cook times
* Fix shift-click behavior for Arcane Workbench and Concocter

v0.0.11-alpha
-------------
* Update research table project background
* Made Flint and Steel set Treefolk on fire
* Add requirement of Sea Dust to Cryotreatment research
* Occlude some research entries from the Upcoming section of the Grimoire
* Add active spell details to wand and scroll tooltips
* Fix source omissions in mana arrow recipes
* Research tables now drop their contents when broken
* Reduced spell cast cooldown to one second, from three seconds
* Reduced effectiveness of Quicken spell mod to 0.15sec per level, from 0.5sec per level
* Increase damage and healing of all spell types
* Double duration of all damage spell secondary effects
* Rebalanced mana costs of most spells

v0.0.10-alpha
-------------
* Increment to Forge version 37.0.59

v0.0.9-alpha
------------
* Add grimoire pages for smelting recipes
* Show widget on research table listing active research aids
* Add upcoming section to grimoire discipline pages
* Add index of unlocked recipes to grimoire

v0.0.8-alpha
------------
* Allow research tables to hold writing implements between uses
* Show spell cooldowns visually
* Record teleport distance statistic
* Properly trigger pixie aggro when attacking with spells
* Fix analysis table button tooltip
* Add more scan entries to the grimoire
* Expand range at which research aids are detected
* Fix bug in wand core research requirements
* Infused stone no longer drops cobblestone, only dust
* Fix silk touch behavior for infused stone
* Increase knockback of Earth-Tinged Arrows
* Only show mana cost grimoire widget when the recipe has a mana cost
* Add workaround for inventory syncing issue with block entities
* Allow items of any durability for theorycrafting materials

v0.0.7-alpha
------------
* Add Mana-tinged Arrows, an early-game improvement to ammunition
* Fix failure to clone player mod data upon death
* Rearrange research tree

v0.0.6-alpha
------------
* Potential fix for dupe bug in inventory block entities

v0.0.5-alpha
------------
* Rework Basic Alchemy requirements
* Reformat recipe pages to make result name more prominent
* Add Sea shrines to icy biomes
* Only show Advanced Wandmaking research after spending some mana
* Fix analysis table crash bug

v0.0.4-alpha
------------
* Added Shield of the Sacred Oath, a ritually empowered shield that reduces all damage you take when blocking
* Fixed bug in aligned wand core mana regen
* Made several tutorial changes
* Increase drop rate of magical saplings from leaves
* Eliminated mana cost for Spellcrafting Altar
* Improve raw marble scan entry
* Improve grimoire navigation
* Start ancient mana fonts at full mana capacity

v0.0.3-alpha
------------
* Initial alpha release
