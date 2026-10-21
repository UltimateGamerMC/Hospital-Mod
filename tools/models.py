#!/usr/bin/env python3
"""Hand-authored block models + all the JSON glue for Hospital Mod.

The three custom models (patient bed, IV drip, heart monitor) are written here as real
box geometry, in the same shape Blockbench exports, and are also dumped as .bbmodel files
so they can be reopened and tweaked in Blockbench.

Run with:  python3 tools/models.py [--mc 26.3|1.21.11]
"""
import argparse
import json
import os

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..")
NS = "hospital-mod"
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", NS)
DATA = os.path.join(ROOT, "src", "main", "resources", "data", NS)
BBMODEL = os.path.join(ROOT, "tools", "bbmodel")

# ------------------------------------------------------------- box helpers ---
ALL_FACES = ("north", "south", "east", "west", "up", "down")


def box(name, frm, to, texture, faces=None):
    """One cuboid. `faces` maps a face name to a texture override."""
    overrides = faces or {}
    face_data = {}
    for face in ALL_FACES:
        face_data[face] = {"uv": [0, 0, 16, 16], "texture": "#" + overrides.get(face, texture)}
    return {"name": name, "from": list(frm), "to": list(to), "faces": face_data}


def model(elements, textures, parent="block/block"):
    return {"parent": parent, "textures": textures, "elements": elements}


# --------------------------------------------------------------- materials ---
STEEL = "equipment_steel"
WHITE = "equipment_white"
DARK = "equipment_dark"
TEAL = "equipment_teal"
MATTRESS = "bed_mattress"
PILLOW = "bed_pillow"
BLANKET = "bed_blanket"
TUBE = "iv_tube"
SCREEN = "monitor_screen"
SCREEN_ON = "monitor_screen_on"
SKIN = "patient_skin"
GOWN = "patient_gown"


def tex(*names):
    return {name: f"{NS}:block/{name}" for name in names}


# ================================================================== BED ======
def bed_elements(occupied):
    parts = [
        box("frame", (1, 3.5, 1), (15, 4.5, 15), STEEL),
        box("mattress", (1.5, 4.5, 1.5), (14.5, 6.5, 14.5), MATTRESS),
        box("headboard", (1, 4.5, 0.5), (15, 11, 1.5), STEEL),
        box("footboard", (1, 4.5, 14.5), (15, 9.5, 15.5), STEEL),
        box("headboard_panel", (2.5, 5.5, 0.45), (13.5, 10, 0.6), WHITE),
        box("rail_west", (0.5, 5.5, 4.5), (1.5, 8.5, 11.5), TEAL),
        box("rail_east", (14.5, 5.5, 4.5), (15.5, 8.5, 11.5), TEAL),
        box("rail_west_bar", (0.4, 8, 4), (1.6, 8.6, 12), STEEL),
        box("rail_east_bar", (14.4, 8, 4), (15.6, 8.6, 12), STEEL),
        box("leg_nw", (1.2, 1, 1.2), (2.2, 3.5, 2.2), STEEL),
        box("leg_ne", (13.8, 1, 1.2), (14.8, 3.5, 2.2), STEEL),
        box("leg_sw", (1.2, 1, 13.8), (2.2, 3.5, 14.8), STEEL),
        box("leg_se", (13.8, 1, 13.8), (14.8, 3.5, 14.8), STEEL),
        box("wheel_nw", (0.8, 0, 0.8), (2.6, 1, 2.6), DARK),
        box("wheel_ne", (13.4, 0, 0.8), (15.2, 1, 2.6), DARK),
        box("wheel_sw", (0.8, 0, 13.4), (2.6, 1, 15.2), DARK),
        box("wheel_se", (13.4, 0, 13.4), (15.2, 1, 15.2), DARK),
        box("call_button", (13, 8.6, 0.35), (14.6, 9.9, 0.95), TEAL),
        box("call_cable", (13.5, 6.6, 0.8), (14.1, 8.6, 1.4), DARK),
    ]
    if occupied:
        # Blanket pulled up over the patient, with a head resting on the pillow.
        parts = [p for p in parts if p["name"] not in ("headboard_panel",)]
        parts += [
            box("pillow", (2.5, 6.5, 1.5), (10.5, 7.6, 5), PILLOW),
            box("blanket", (1.5, 6.4, 6.6), (14.5, 7.6, 14.5), BLANKET),
            box("patient_gown", (3.4, 6.9, 6.2), (12.6, 9.1, 7.4), GOWN),
            box("patient_head", (4.6, 7.6, 1.7), (11.4, 13.6, 5.9), SKIN),
            box("patient_hair", (4.4, 12.9, 1.5), (11.6, 14.3, 6.1), DARK),
        ]
    else:
        parts += [
            box("pillow", (2.5, 6.5, 1.5), (10.5, 7.6, 5), PILLOW),
            box("blanket", (1.5, 6.4, 6.6), (14.5, 7.1, 14.5), BLANKET),
        ]
    return parts


# ============================================================== IV STAND =====
def iv_elements(bag_texture, screen_texture):
    return [
        box("base_a", (1.5, 0.6, 7), (14.5, 1.6, 9), DARK),
        box("base_b", (7, 0.6, 1.5), (9, 1.6, 14.5), DARK),
        box("wheel_w", (1, 0, 6.6), (3, 1, 9.4), DARK),
        box("wheel_e", (13, 0, 6.6), (15, 1, 9.4), DARK),
        box("wheel_n", (6.6, 0, 1), (9.4, 1, 3), DARK),
        box("wheel_s", (6.6, 0, 13), (9.4, 1, 15), DARK),
        box("pole", (7.2, 1.6, 7.2), (8.8, 27, 8.8), STEEL),
        box("pole_cap", (7, 27, 7), (9, 27.6, 9), DARK),
        box("collar", (6.8, 20, 6.8), (9.2, 21.6, 9.2), STEEL),
        box("hook_arm", (4.4, 26, 7.4), (8.8, 27.2, 8.6), STEEL),
        box("hook_tip", (4.4, 25.2, 7.4), (5, 26.2, 8.6), STEEL),
        box("bag_ring", (4.5, 25.6, 7.5), (5.9, 26.4, 8.5), STEEL),
        box("bag", (3.4, 17, 6.2), (7.2, 25.6, 9.8), bag_texture),
        box("tube", (4.6, 7.4, 7.5), (5.4, 17.2, 8.5), TUBE),
        box("tube_needle", (4.6, 6.8, 7.6), (5.4, 7.4, 8.4), DARK),
        box("pump", (8.8, 12, 6.4), (12.4, 16.4, 9.6), WHITE),
        box("pump_screen", (12.4, 13.4, 6.9), (12.56, 15.6, 9.1), SCREEN,
            faces={"east": screen_texture}),
        box("pump_button", (8.9, 12.2, 9.6), (12.3, 13, 9.76), TEAL),
    ]


# ========================================================= HEART MONITOR =====
def monitor_elements(screen_texture, led_texture):
    return [
        box("wheel_nw", (2.4, 0, 2.4), (4.4, 1.2, 4.4), DARK),
        box("wheel_ne", (11.6, 0, 2.4), (13.6, 1.2, 4.4), DARK),
        box("wheel_sw", (2.4, 0, 11.6), (4.4, 1.2, 13.6), DARK),
        box("wheel_se", (11.6, 0, 11.6), (13.6, 1.2, 13.6), DARK),
        box("base", (2, 1.2, 2), (14, 2.4, 14), STEEL),
        box("column", (6.8, 2.4, 6.8), (9.2, 12, 9.2), STEEL),
        box("cable", (8.2, 2.4, 9.4), (8.8, 12, 10), DARK),
        box("body", (2.4, 12, 5.6), (13.6, 22, 10.4), WHITE),
        box("screen", (3.2, 13.4, 5.42), (12.8, 20.6, 5.6), SCREEN,
            faces={"north": screen_texture}),
        box("bezel", (3.2, 12.8, 5.5), (12.8, 13.4, 5.66), TEAL),
        box("knob_1", (3.4, 13.6, 10.4), (4.6, 14.8, 10.62), DARK),
        box("knob_2", (5, 13.6, 10.4), (6.2, 14.8, 10.62), DARK),
        box("knob_3", (6.6, 13.6, 10.4), (7.8, 14.8, 10.62), DARK),
        box("handle", (3, 22, 7.2), (13, 23.2, 8.8), STEEL),
        box("led", (12, 21, 5.7), (13.2, 21.8, 6.3), led_texture),
    ]


# ------------------------------------------------------------- model table ---
def build_models():
    models = {}
    models["hospital_bed"] = model(bed_elements(False), tex(STEEL, MATTRESS, PILLOW, BLANKET, WHITE, TEAL, DARK))
    models["hospital_bed_occupied"] = model(bed_elements(True), tex(STEEL, MATTRESS, PILLOW, BLANKET, WHITE, TEAL, DARK, SKIN, GOWN))
    models["iv_stand"] = model(iv_elements("iv_bag_full", SCREEN_ON), tex(STEEL, DARK, WHITE, TEAL, TUBE, SCREEN, SCREEN_ON, "iv_bag_full"))
    models["iv_stand_half"] = model(iv_elements("iv_bag_half", SCREEN_ON), tex(STEEL, DARK, WHITE, TEAL, TUBE, SCREEN, SCREEN_ON, "iv_bag_half"))
    models["iv_stand_empty"] = model(iv_elements("iv_bag_empty", SCREEN), tex(STEEL, DARK, WHITE, TEAL, TUBE, SCREEN, SCREEN_ON, "iv_bag_empty"))
    models["heart_monitor"] = model(monitor_elements(SCREEN, DARK), tex(STEEL, DARK, WHITE, TEAL, SCREEN))
    models["heart_monitor_on"] = model(monitor_elements(SCREEN_ON, TEAL), tex(STEEL, DARK, WHITE, TEAL, SCREEN, SCREEN_ON))

    # Plain cube blocks reuse vanilla parents.
    models["hospital_floor"] = {"parent": "minecraft:block/cube_all", "textures": {"all": f"{NS}:block/hospital_floor"}}
    models["hospital_wall"] = {"parent": "minecraft:block/cube_all", "textures": {"all": f"{NS}:block/hospital_wall"}}
    models["medical_cabinet"] = {
        "parent": "minecraft:block/orientable",
        "textures": {
            "top": f"{NS}:block/medical_cabinet_top",
            "side": f"{NS}:block/medical_cabinet_side",
            "front": f"{NS}:block/medical_cabinet_front",
        },
    }
    return models


def blockstates():
    return {
        "hospital_floor": {"variants": {"": {"model": f"{NS}:block/hospital_floor"}}},
        "hospital_wall": {"variants": {"": {"model": f"{NS}:block/hospital_wall"}}},
        "hospital_bed": {"variants": {
            f"facing={facing},occupied={occ}": {
                "model": f"{NS}:block/hospital_bed" + ("_occupied" if occ == "true" else ""),
                "y": y,
            }
            for facing, y in (("north", 0), ("east", 90), ("south", 180), ("west", 270))
            for occ in ("false", "true")
        }},
        "iv_stand": {"variants": {
            f"facing={facing},charge={charge}": {"model": f"{NS}:block/{model_for_charge(charge)}", "y": y}
            for facing, y in (("north", 0), ("east", 90), ("south", 180), ("west", 270))
            for charge in range(5)
        }},
        "heart_monitor": {"variants": {
            f"facing={facing},powered={powered}": {
                "model": f"{NS}:block/heart_monitor" + ("_on" if powered == "true" else ""),
                "y": y,
            }
            for facing, y in (("north", 0), ("east", 90), ("south", 180), ("west", 270))
            for powered in ("false", "true")
        }},
        "medical_cabinet": {"variants": {
            f"facing={facing}": {"model": f"{NS}:block/medical_cabinet", "y": y}
            for facing, y in (("north", 0), ("east", 90), ("south", 180), ("west", 270))
        }},
    }


def model_for_charge(charge):
    if charge == 0:
        return "iv_stand_empty"
    if charge <= 2:
        return "iv_stand_half"
    return "iv_stand"


BLOCKS = ["hospital_floor", "hospital_wall", "medical_cabinet", "hospital_bed", "iv_stand", "heart_monitor"]
TOOLS = ["bandage", "medkit", "painkillers", "saline_bag", "syringe", "stethoscope", "defibrillator", "scalpel"]


# ------------------------------------------------------------------ recipes --
def recipes():
    out = {}
    out["hospital_floor"] = shaped(["WWW", "SQS", "WWW"], {"W": "minecraft:white_concrete", "S": "minecraft:stone", "Q": "minecraft:quartz"}, "hospital_floor", 8)
    out["hospital_wall"] = shaped(["WWW", "WQW", "WWW"], {"W": "minecraft:white_concrete", "Q": "minecraft:quartz"}, "hospital_wall", 8)
    out["hospital_bed"] = shaped(["WWW", "III", "I I"], {"W": "minecraft:white_wool", "I": "minecraft:iron_ingot"}, "hospital_bed", 1)
    out["iv_stand"] = shaped(["I I", " G ", "I I"], {"I": "minecraft:iron_ingot", "G": "minecraft:glass_bottle"}, "iv_stand", 1)
    out["heart_monitor"] = shaped(["IRI", "RGR", "III"], {"I": "minecraft:iron_ingot", "R": "minecraft:redstone", "G": "minecraft:glass_pane"}, "heart_monitor", 1)
    out["medical_cabinet"] = shaped(["III", "IWI", "ICI"], {"I": "minecraft:iron_ingot", "W": "minecraft:white_wool", "C": "minecraft:chest"}, "medical_cabinet", 1)
    out["bandage"] = shapeless(["minecraft:paper", "minecraft:string", "minecraft:string"], "bandage", 2)
    out["medkit"] = shaped(["BPB", "BWB", "BPB"], {"B": f"{NS}:bandage", "P": "minecraft:paper", "W": "minecraft:white_wool"}, "medkit", 1)
    out["painkillers"] = shapeless(["minecraft:sugar", "minecraft:paper", "minecraft:white_dye"], "painkillers", 2)
    out["saline_bag"] = shapeless(["minecraft:glass_bottle", "minecraft:glass_bottle", "minecraft:sugar", "minecraft:water_bucket"], "saline_bag", 1)
    out["syringe"] = shaped(["  I", " G ", "N  "], {"I": "minecraft:iron_nugget", "G": "minecraft:glass_bottle", "N": "minecraft:iron_ingot"}, "syringe", 1)
    out["stethoscope"] = shaped(["I I", " S ", " G "], {"I": "minecraft:iron_nugget", "S": "minecraft:string", "G": "minecraft:gold_nugget"}, "stethoscope", 1)
    out["defibrillator"] = shaped(["IRI", "RGR", "III"], {"I": "minecraft:iron_ingot", "R": "minecraft:redstone", "G": "minecraft:copper_ingot"}, "defibrillator", 1)
    out["scalpel"] = shaped(["  I", " I ", "S  "], {"I": "minecraft:iron_ingot", "S": "minecraft:stick"}, "scalpel", 1)
    return out


def shaped(pattern, keys, result, count):
    return {"type": "minecraft:crafting_shaped", "pattern": pattern,
            "key": keys, "result": {"id": f"{NS}:{result}", "count": count}}


def shapeless(ingredients, result, count):
    return {"type": "minecraft:crafting_shapeless",
            "ingredients": list(ingredients),
            "result": {"id": f"{NS}:{result}", "count": count}}


def advancement(recipe_id, items):
    criteria = {}
    for index, item in enumerate(items):
        criteria[f"has_{index}"] = {
            "trigger": "minecraft:inventory_changed",
            "conditions": {"items": [{"items": [item]}]},
        }
    return {"criteria": criteria,
            "requirements": [list(criteria.keys())],
            "rewards": {"recipes": [f"{NS}:{recipe_id}"]}}


def loot_table(block, mc):
    entry = {"type": "minecraft:item", "name": f"{NS}:{block}"}
    if mc == "26.3":
        return {"type": "minecraft:block", "pools": [
            {"rolls": 1, "condition": {"type": "minecraft:survives_explosion"}, "entries": [entry]}]}
    return {"type": "minecraft:block", "pools": [
        {"rolls": 1, "conditions": [{"condition": "minecraft:survives_explosion"}], "entries": [entry]}]}


LANG = {
    "block.hospital-mod.hospital_floor": "Hospital Floor",
    "block.hospital-mod.hospital_wall": "Hospital Wall",
    "block.hospital-mod.medical_cabinet": "Supply Cabinet",
    "block.hospital-mod.hospital_bed": "Hospital Bed",
    "block.hospital-mod.iv_stand": "IV Drip Stand",
    "block.hospital-mod.heart_monitor": "Heart Monitor",
    "item.hospital-mod.bandage": "Bandage",
    "item.hospital-mod.medkit": "Medkit",
    "item.hospital-mod.painkillers": "Painkillers",
    "item.hospital-mod.saline_bag": "Saline Bag",
    "item.hospital-mod.syringe": "Syringe",
    "item.hospital-mod.stethoscope": "Stethoscope",
    "item.hospital-mod.defibrillator": "Defibrillator",
    "item.hospital-mod.scalpel": "Scalpel",
    "itemGroup.hospital-mod.hospital": "Hospital",
    "msg.hospital-mod.rested": "You lie back. Your vitals steady and this bed is now your respawn point.",
    "msg.hospital-mod.got_up": "You get up.",
    "msg.hospital-mod.bandaged": "Bandaged up. That stings less already.",
    "msg.hospital-mod.medkitted": "Full treatment. Wounds closed and debuffs cleared.",
    "msg.hospital-mod.painkillers": "Painkillers kick in. Everything goes quiet for a while.",
    "msg.hospital-mod.already_healthy": "Nothing to treat right now.",
    "msg.hospital-mod.injected_self": "Adrenaline! Your heart hammers and you feel fast.",
    "msg.hospital-mod.injected_target": "You inject %s with a stimulant.",
    "msg.hospital-mod.sedated": "You sedate %s. It slows right down.",
    "msg.hospital-mod.defibrillated": "Clear! %s is back to full health.",
    "msg.hospital-mod.no_patient": "No critical patient in range.",
    "msg.hospital-mod.monitor_none": "Heart monitor: no patient detected.",
    "msg.hospital-mod.iv_refilled": "Saline bag hung. The drip restarts.",
    "msg.hospital-mod.iv_empty": "The IV bag is empty. Use a saline bag to refill it.",
    "msg.hospital-mod.iv_full": "That bag is already full.",
    "msg.hospital-mod.iv_level": "IV bag: %s/%s doses left.",
    "msg.hospital-mod.cabinet_used": "You take some supplies from the cabinet.",
    "msg.hospital-mod.cabinet_restocking": "The cabinet is restocking... %ss",
    "msg.hospital-mod.command_give": "Hospital kit added to your inventory.",
    "msg.hospital-mod.command_heal": "Treated. Cleared %s debuffs.",
    "msg.hospital-mod.command_room": "Ward built. Two beds, two drips, a monitor and a cabinet.",
}


# --------------------------------------------------------------- bbmodel -----
def bbmodel(name, elements, textures, texture_files):
    entries = []
    for element in elements:
        faces = {}
        for face, data in element["faces"].items():
            tex_name = data["texture"].lstrip("#")
            faces[face] = {"uv": data["uv"], "texture": texture_files.index(tex_name)}
        entries.append({"name": element["name"], "from": element["from"], "to": element["to"],
                        "origin": [8, 8, 8], "faces": faces, "type": "cube"})
    return {
        "meta": {"format_version": "4.10", "model_format": "java_block", "box_uv": False},
        "name": name,
        "parent": "block/block",
        "ambientocclusion": True,
        "front_gui_light": False,
        "visible_box": [0, 0, 0],
        "resolution": {"width": 16, "height": 16},
        "elements": entries,
        "textures": [
            {"path": "", "name": f"{tex}.png", "folder": "block", "namespace": NS, "id": str(i), "particle": i == 0}
            for i, tex in enumerate(texture_files)
        ],
    }


def write(path, payload):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w") as handle:
        json.dump(payload, handle, indent=2)
        handle.write("\n")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--mc", default="26.3")
    args = parser.parse_args()

    models = build_models()
    for name, payload in models.items():
        write(os.path.join(ASSETS, "models", "block", name + ".json"), payload)
    for name, payload in blockstates().items():
        write(os.path.join(ASSETS, "blockstates", name + ".json"), payload)

    # Item model + the 1.21.4+ item definition file for every item and block item.
    for name in TOOLS:
        write(os.path.join(ASSETS, "models", "item", name + ".json"),
              {"parent": "item/generated", "textures": {"layer0": f"{NS}:item/{name}"}})
        write(os.path.join(ASSETS, "items", name + ".json"),
              {"model": {"type": "minecraft:model", "model": f"{NS}:item/{name}"}})
    for name in BLOCKS:
        write(os.path.join(ASSETS, "models", "item", name + ".json"),
              {"parent": f"{NS}:block/{name}"})
        write(os.path.join(ASSETS, "items", name + ".json"),
              {"model": {"type": "minecraft:model", "model": f"{NS}:block/{name}"}})

    # Loot tables, recipes and the recipe-unlock advancements.
    recipe_data = recipes()
    for name in BLOCKS:
        write(os.path.join(DATA, "loot_table", "blocks", name + ".json"), loot_table(name, args.mc))
    for name, payload in recipe_data.items():
        write(os.path.join(DATA, "recipe", name + ".json"), payload)
        keys = payload["key"].values() if payload["type"] == "minecraft:crafting_shaped" else payload["ingredients"]
        write(os.path.join(DATA, "advancement", "recipes", "misc", name + ".json"),
              advancement(name, sorted(set(keys))))

    write(os.path.join(ASSETS, "lang", "en_us.json"), LANG)

    # Blockbench sources for the three custom models.
    write(os.path.join(BBMODEL, "hospital_bed.bbmodel"),
          bbmodel("hospital_bed", bed_elements(False), None, [STEEL, MATTRESS, PILLOW, BLANKET, WHITE, TEAL, DARK]))
    write(os.path.join(BBMODEL, "hospital_bed_occupied.bbmodel"),
          bbmodel("hospital_bed_occupied", bed_elements(True), None, [STEEL, MATTRESS, PILLOW, BLANKET, WHITE, TEAL, DARK, SKIN, GOWN]))
    write(os.path.join(BBMODEL, "iv_stand.bbmodel"),
          bbmodel("iv_stand", iv_elements("iv_bag_full", SCREEN_ON), None, [STEEL, DARK, WHITE, TEAL, TUBE, SCREEN, SCREEN_ON, "iv_bag_full"]))
    write(os.path.join(BBMODEL, "heart_monitor.bbmodel"),
          bbmodel("heart_monitor", monitor_elements(SCREEN_ON, TEAL), None, [STEEL, DARK, WHITE, TEAL, SCREEN, SCREEN_ON]))

    print(f"wrote {len(models)} block models, {len(BLOCKS)} blockstates, {len(recipe_data)} recipes, lang, loot tables ({args.mc}) and 4 bbmodels")


if __name__ == "__main__":
    main()
