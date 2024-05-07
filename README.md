<p align="center">
  <img src="https://raw.githubusercontent.com/Traverse-Joe/Tavern-Tokens/assets/banner.png">
</p>

# Tavern Tokens

Tavern Tokens is a mod that adds a unique currency system to the game based on tangible items. It introduces a custom wallet designed to offer players a convenient way to store large sums of coins. The limit, you may ask? The Java Long Limit or about 9 quintillion items. This wallet is saved to the player's data, so they can easily access it whenever they want. 

One of the best things about this mod is that it empowers modders and developers to create their own custom currency that can be stored in the wallet. This mod includes a custom item tag to simplify adding new currencies to the wallet.

### Quick note for our mod and modpack developers.
If you wish to create your own currency, make sure to add the tag `taverntokens:valid_currency` to any items you like to be placed in the wallet. Also note that any currency that is placed in the wallet will be stripped of all other NBT tags. This means if you want to create an item with a custom lore or display **YOU MUST** mod that within your item.

## Requirements

Tavern Tokens requires the **Fabric API** to work. As for right now, this mod is only made for fabric. This means that **Forge** will _not_ work.

## Download
You can find the downloads for each version with their release notes in the [releases page](https://github.com/Traverse-Joe/Tavern-Tokens/releases). Or alternatively, you can also download it from [Modrinth](https://modrinth.com/project/tavern-tokens) or [CurseForge](https://curseforge.com/minecraft/mc-mods/tavern-tokens).


## Compiling

Tavern Tokens uses Gradle when compiling. If you would like to build your own version, here's how to do so:
```bash
./gradlew clean build # on UNIX-based systems (mac, linux)
gradlew clean build # on Windows
```
You can get source code from the [releases page](https://github.com/Traverse-Joe/Tavern-Tokens/releases). You may also clone this repository, but that code may or may not be stable.