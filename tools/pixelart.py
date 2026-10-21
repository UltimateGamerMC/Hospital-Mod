#!/usr/bin/env python3
"""Hand-placed pixel art for Hospital Mod.

Every texture is authored here as a 16x16 character grid and painted pixel by pixel.
Run with:  python3 tools/pixelart.py
Outputs PNGs into src/main/resources/assets/hospital-mod/textures/.
"""
import os

from PIL import Image

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..")
TEX = os.path.join(ROOT, "src", "main", "resources", "assets", "hospital-mod", "textures")

# ---------------------------------------------------------------- palette ----
PALETTE = {
    ".": (0, 0, 0, 0),          # transparent
    "0": (14, 22, 32, 255),     # outline / near black
    "1": (27, 42, 56, 255),     # dark slate
    "2": (46, 68, 83, 255),     # slate
    "3": (71, 89, 107, 255),    # gray blue
    "4": (107, 124, 140, 255),  # mid gray
    "5": (147, 163, 176, 255),  # light gray
    "6": (195, 205, 214, 255),  # pale gray
    "7": (237, 242, 246, 255),  # white
    "8": (255, 255, 255, 255),  # pure white
    "a": (23, 184, 166, 255),   # teal
    "b": (14, 143, 130, 255),   # dark teal
    "c": (127, 227, 216, 255),  # light teal
    "d": (62, 143, 191, 255),   # blue
    "e": (42, 108, 150, 255),   # dark blue
    "f": (156, 200, 232, 255),  # light blue
    "g": (224, 90, 90, 255),    # red
    "h": (166, 58, 58, 255),    # dark red
    "i": (242, 193, 78, 255),   # amber
    "j": (232, 163, 61, 255),   # dark amber
    "k": (111, 168, 111, 255),  # green
    "l": (63, 122, 63, 255),    # dark green
    "m": (57, 255, 158, 255),   # ecg green
    "n": (232, 176, 138, 255),  # skin
    "o": (201, 143, 106, 255),  # skin shadow
    "p": (43, 33, 24, 255),     # dark hair
    "q": (139, 94, 60, 255),    # brown
    "r": (90, 58, 34, 255),     # dark brown
    "s": (176, 216, 232, 255),  # pale cyan
    "t": (127, 196, 220, 255),  # fluid
    "u": (217, 228, 236, 255),  # very pale
    "v": (148, 167, 180, 255),  # shadow gray
}

ART = {}

# ------------------------------------------------------- block materials -----
ART["block/equipment_steel"] = [
    "5555555555555555",
    "5666666666666655",
    "5666666666666655",
    "5555555555555555",
    "5444444444444445",
    "5444444444444445",
    "5444444444444445",
    "5555555555555555",
    "5666666666666655",
    "5666666666666655",
    "5555555555555555",
    "5444444444444445",
    "5444444444444445",
    "5444444444444445",
    "5555555555555555",
    "5000000000000005",
]

ART["block/equipment_white"] = [
    "7777777777777777",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7777777777777777",
    "7666666666666667",
    "7666666666666667",
    "7666666666666667",
    "7666666666666667",
    "7777777777777777",
    "7666666666666667",
    "7666666666666667",
    "7666666666666667",
    "7777777777777777",
]

ART["block/equipment_dark"] = [
    "1111111111111111",
    "1222222222222211",
    "1222222222222211",
    "1211111111111211",
    "1211111111111211",
    "1211111111111211",
    "1211111111111211",
    "1211111111111211",
    "1222222222222211",
    "1222222222222211",
    "1111111111111111",
    "1222222222222211",
    "1222222222222211",
    "1211111111111211",
    "1211111111111211",
    "1111111111111111",
]

ART["block/equipment_teal"] = [
    "aaaaaaaaaaaaaaaa",
    "acccccccccccccaa",
    "acccccccccccccaa",
    "aaaaaaaaaaaaaaaa",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "aaaaaaaaaaaaaaaa",
    "acccccccccccccaa",
    "acccccccccccccaa",
    "aaaaaaaaaaaaaaaa",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "aaaaaaaaaaaaaaaa",
    "a00000000000000a",
]

ART["block/bed_mattress"] = [
    "ffffffffffffffff",
    "f66666666666666f",
    "f6f6f6f6f6f6f6ff",
    "f66666666666666f",
    "f6f6f6f6f6f6f6ff",
    "f66666666666666f",
    "f6f6f6f6f6f6f6ff",
    "f66666666666666f",
    "f6f6f6f6f6f6f6ff",
    "f66666666666666f",
    "f6f6f6f6f6f6f6ff",
    "f66666666666666f",
    "f6f6f6f6f6f6f6ff",
    "f66666666666666f",
    "f6f6f6f6f6f6f6ff",
    "ffffffffffffffff",
]

ART["block/bed_pillow"] = [
    "7777777777777777",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888777777778877",
    "7887777777777877",
    "7887777777777877",
    "7888777777778877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7666666666666667",
    "7777777777777777",
]

ART["block/bed_blanket"] = [
    "aaaaaaaaaaaaaaaa",
    "acccccccccccccaa",
    "acccccccccccccaa",
    "aaaaaaaaaaaaaaaa",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "aaaaaaaaaaaaaaaa",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "abbbbbbbbbbbbbba",
    "aaaaaaaaaaaaaaaa",
    "a00000000000000a",
]

ART["block/iv_tube"] = [
    "ssssssssssssssss",
    "stttttttttttttts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stssssssssssssts",
    "stttttttttttttts",
    "ssssssssssssssss",
]

ART["block/iv_bag_full"] = [
    "7777777777777777",
    "7ssssssssssssss7",
    "7ss0000000000ss7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s000000000000s7",
    "7ssssssssssssss7",
]

ART["block/iv_bag_half"] = [
    "7777777777777777",
    "7ssssssssssssss7",
    "7ss0000000000ss7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s0tttttttttt0s7",
    "7s000000000000s7",
    "7ssssssssssssss7",
]

ART["block/iv_bag_empty"] = [
    "7777777777777777",
    "7ssssssssssssss7",
    "7ss0000000000ss7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s0ssssssssss0s7",
    "7s000000000000s7",
    "7ssssssssssssss7",
]

ART["block/monitor_screen"] = [
    "0000000000000000",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111122222211110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0000000000000000",
]

ART["block/monitor_screen_on"] = [
    "0000000000000000",
    "0111111111111110",
    "0111111111111110",
    "01111111m1111110",
    "01111111m1111110",
    "01111111m1111110",
    "01111111m1111110",
    "01111111m1111110",
    "0mmmmmm1m1mmmmm0",
    "0111111m1m111110",
    "0111111m1m111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0111111111111110",
    "0000000000000000",
]

ART["block/patient_skin"] = [
    "nnnnnnnnnnnnnnnn",
    "nnnnnnnnnnnnnnnn",
    "nnnnnnnnnnnnnnnn",
    "nnnnnnnnnnnnnnnn",
    "nnnnnnnnnnnnnnnn",
    "nnnnnnnnnnnnnnnn",
    "nnnnnnnnnnnnnnnn",
    "nnnnnnnnnnnnnnnn",
    "oooooooooooooooo",
    "oooooooooooooooo",
    "oooooooooooooooo",
    "oooooooooooooooo",
    "oooooooooooooooo",
    "oooooooooooooooo",
    "oooooooooooooooo",
    "oooooooooooooooo",
]

ART["block/patient_gown"] = [
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuaaaaaaaaaaauu",
    "uuuaaaaaaaaaaauu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
    "uuuuuuuuuuuuuuuu",
]

# ---------------------------------------------------------- placeable blocks -
ART["block/hospital_floor"] = [
    "6666666666666666",
    "6777u7u767u77776",
    "67u77777677777u6",
    "6777777767u77776",
    "6777u77767777776",
    "677777776777u776",
    "67u7777767777776",
    "677777u767777776",
    "6666666666666666",
    "67u7777767777u76",
    "6777777767u77776",
    "677777u767777776",
    "6777u777677777u6",
    "6777777767777776",
    "67u7777767u77776",
    "6777777767777776",
]

ART["block/hospital_wall"] = [
    "7777777677777776",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7888888688888886",
    "7777777677777776",
    "6666666566666665",
    "6uuuuuu5uuuuuuu5",
    "6uuuuuu5uuuuuuu5",
    "5555555555555555",
]

ART["block/medical_cabinet_front"] = [
    "0000000000000000",
    "0777777777777770",
    "0777777777777770",
    "0777777777777770",
    "0777777aa7777770",
    "077777aaaa777770",
    "07777aaaaaa77770",
    "077777aaaa777770",
    "0777777aa7777770",
    "0777777777777770",
    "0000000000000000",
    "0777777777777770",
    "0777666666667770",
    "0777666666667770",
    "0777777777777770",
    "0000000000000000",
]

ART["block/medical_cabinet_side"] = [
    "7777777777777777",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7777777777777777",
    "7666666666666667",
    "7666666666666667",
    "7666666666666667",
    "7777777777777777",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7777777777777777",
]

ART["block/medical_cabinet_top"] = [
    "7777777777777777",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7888888888888877",
    "7777777777777777",
]

# ------------------------------------------------------------------- items ---
ART["block/sharps_body"] = [
    "hhhhhhhhhhhhhhhh",
    "hggggggggggggggh",
    "hggggggggggggggh",
    "hggggggggggggggh",
    "hhhhhhhhhhhhhhhh",
    "hggggggggggggggh",
    "hggggggggggggggh",
    "hggggggggggggggh",
    "hhhhhhhhhhhhhhhh",
    "hggggggggggggggh",
    "hggggggggggggggh",
    "hggggggggggggggh",
    "hhhhhhhhhhhhhhhh",
    "hggggggggggggggh",
    "hggggggggggggggh",
    "hhhhhhhhhhhhhhhh",
]

ART["block/sharps_lid"] = [
    "2222222222222222",
    "2111111111111112",
    "2111111111111112",
    "2222222222222222",
    "2111111111111112",
    "2111111111111112",
    "2111111111111112",
    "2222222222222222",
    "2111111111111112",
    "2111111111111112",
    "2222222222222222",
    "2111111111111112",
    "2111111111111112",
    "2111111111111112",
    "2222222222222222",
    "2000000000000002",
]

ART["block/sharps_window_empty"] = [
    "0000000000000000",
    "0uuuuuuuuuuuuuu0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0uuuuuuuuuuuuuu0",
    "0000000000000000",
]

ART["block/sharps_window_low"] = [
    "0000000000000000",
    "0uuuuuuuuuuuuuu0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0ujjjjjjjjjjjju0",
    "0ujjjjjjjjjjjju0",
    "0uuuuuuuuuuuuuu0",
    "0000000000000000",
]

ART["block/sharps_window_half"] = [
    "0000000000000000",
    "0uuuuuuuuuuuuuu0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0u777777777777u0",
    "0ujjjjjjjjjjjju0",
    "0ujjjjjjjjjjjju0",
    "0ujjjjjjjjjjjju0",
    "0ujjjjjjjjjjjju0",
    "0ujjjjjjjjjjjju0",
    "0ujjjjjjjjjjjju0",
    "0uuuuuuuuuuuuuu0",
    "0000000000000000",
]

ART["block/sharps_window_full"] = [
    "0000000000000000",
    "0uuuuuuuuuuuuuu0",
    "0uggggggggggggu0",
    "0uggggggsgggggu0",
    "0ugggggsggggggu0",
    "0uggggsgggggggu0",
    "0ugggsggggggggu0",
    "0uggsgggggggggu0",
    "0uggggggggggggu0",
    "0uggggggggggggu0",
    "0uggggggggggggu0",
    "0uggggggggggggu0",
    "0uggggggggggggu0",
    "0uggggggggggggu0",
    "0uuuuuuuuuuuuuu0",
    "0000000000000000",
]

ART["item/biohazard_bag"] = [
    "................",
    "......00........",
    ".....0gg0.......",
    "....0gggg0......",
    "..00iiiiii00....",
    "..0iiiiiiii0....",
    "..0ii00ii0i0....",
    "..0i000000i0....",
    "..0i000000i0....",
    "..0ii0000ii0....",
    "..0iii00iii0....",
    "..0iiiiiiii0....",
    "..0iiiiiiii0....",
    "..0iiiiiiii0....",
    "..0000000000....",
    "................",
]

ART["item/bandage"] = [
    "................",
    "................",
    "..000000000000..",
    "..077777777770..",
    "..077666667770..",
    "..076666666670..",
    "..076666666670..",
    "..077666667770..",
    "..077777777770..",
    "..000000000000..",
    ".....077770.....",
    ".....077770.....",
    ".....077770.....",
    "......07770.....",
    "......00000.....",
    "................",
]

ART["item/medkit"] = [
    "................",
    ".....000000.....",
    "....01111110....",
    "..000000000000..",
    "..077777777770..",
    "..077777777770..",
    "..07777aa77770..",
    "..0777aaaa7770..",
    "..077aaaaaa770..",
    "..0777aaaa7770..",
    "..07777aa77770..",
    "..077777777770..",
    "..066666666660..",
    "..000000000000..",
    "................",
    "................",
]

ART["item/painkillers"] = [
    "................",
    ".....000000.....",
    "....01111110....",
    "....07777770....",
    "....0aaaaaa0....",
    "....0aaaaaa0....",
    "...0000000000...",
    "...08888888880..",
    "...088g88g8880..",
    "...08888888880..",
    "...0888g88g880..",
    "...08888888880..",
    "...088g88g8880..",
    "...08888888880..",
    "...0000000000...",
    "................",
]

ART["item/saline_bag"] = [
    "................",
    ".....0....0.....",
    ".....00..00.....",
    "....0..00..0....",
    "....0ssssss0....",
    "....0s0000s0....",
    "....0s0tt0s0....",
    "....0s0tt0s0....",
    "....0s0tt0s0....",
    "....0s0tt0s0....",
    "....0s0tt0s0....",
    "....0s0000s0....",
    "....0ssssss0....",
    ".....0s00s0.....",
    "......0000......",
    ".......00.......",
]

ART["item/syringe"] = [
    "................",
    "................",
    "................",
    "................",
    "................",
    "..0000000000....",
    "gg000tttt770....",
    "gg000tttt7700555",
    "gg000tttt7700555",
    "gg000tttt770....",
    "..0000000000....",
    "................",
    "................",
    "................",
    "................",
    "................",
]

ART["item/stethoscope"] = [
    "................",
    "...0........0...",
    "..0g0......0g0..",
    "..0g0......0g0..",
    "...0g0....0g0...",
    "....0g0..0g0....",
    ".....0g00g0.....",
    "......0gg0......",
    ".......0g0......",
    ".......0g0......",
    "......05550.....",
    ".....0555550....",
    "....055555550...",
    "....055555550...",
    "....055555550...",
    ".....0000000....",
]

ART["item/defibrillator"] = [
    "................",
    "..0000....0000..",
    ".0gggg0..0gggg0.",
    ".0g77g0..0g77g0.",
    ".0g77g0..0g77g0.",
    ".0gggg0..0gggg0.",
    ".0g00g0..0g00g0.",
    "..0..0....0..0..",
    "..0..0....0..0..",
    "...0.0....0.0...",
    "....0.0000.0....",
    "....0.0110.0....",
    ".....0000000....",
    "......07770.....",
    "......07770.....",
    ".......000......",
]

ART["item/scalpel"] = [
    "................",
    "............000.",
    "...........08880",
    "..........08880.",
    ".........08880..",
    "........08880...",
    ".......08880....",
    "......08880.....",
    ".....05550......",
    "....05550.......",
    "...05550........",
    "..05550.........",
    "..0550..........",
    "..050...........",
    "..00............",
    "................",
]


def biohazard_rows(size=16):
    """The biohazard trefoil: three lobes around a centre ring, placed pixel by pixel."""
    import math

    centre = (size - 1) / 2.0
    lobes = [(90.0, 3.7, 3.1), (210.0, 3.7, 3.1), (330.0, 3.7, 3.1)]
    rows = []
    for y in range(size):
        row = ""
        for x in range(size):
            ink = False
            for angle, distance, radius in lobes:
                a = math.radians(angle)
                lx = centre + math.cos(a) * distance
                ly = centre - math.sin(a) * distance
                d = math.hypot(x - lx, y - ly)
                if d <= radius:
                    ink = True
                if d <= radius * 0.40:
                    ink = False
            dc = math.hypot(x - centre, y - centre)
            if dc <= 2.4:
                ink = True
            if dc <= 1.1:
                ink = False
            row += "0" if ink else "i"
        rows.append(row)
    return rows


ART["block/sharps_hazard"] = biohazard_rows()


def paint(name, rows):
    """Turn one character grid into a PNG."""
    if len(rows) != 16 or any(len(r) != 16 for r in rows):
        raise SystemExit(f"{name}: art must be exactly 16 rows of 16 characters")
    image = Image.new("RGBA", (16, 16))
    pixels = []
    for row in rows:
        for char in row:
            if char not in PALETTE:
                raise SystemExit(f"{name}: unknown palette character {char!r}")
            pixels.append(PALETTE[char])
    image.putdata(pixels)
    path = os.path.join(TEX, name + ".png")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    image.save(path)
    return path


def make_icon():
    """A 128x128 project icon: teal plate, white cross, live ECG line."""
    size = 128
    image = Image.new("RGBA", (size, size))
    pixels = []
    for y in range(size):
        for x in range(size):
            # Diagonal teal gradient background.
            blend = (x + y) / (2 * size - 2)
            r = int(10 + blend * 18)
            g = int(60 + blend * 90)
            b = int(70 + blend * 80)
            # Rounded corner mask.
            cx = min(x, size - 1 - x)
            cy = min(y, size - 1 - y)
            if cx * cx + cy * cy < 12 * 12:
                pixels.append((0, 0, 0, 0))
                continue
            pixels.append((r, g, b, 255))
    image.putdata(pixels)

    draw = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    from PIL import ImageDraw

    pen = ImageDraw.Draw(draw)
    # White medical cross with a soft shadow.
    bar, arm = 22, 52
    pen.rectangle([size // 2 - bar // 2, size // 2 - arm, size // 2 + bar // 2, size // 2 + arm], fill=(255, 255, 255, 255))
    pen.rectangle([size // 2 - arm, size // 2 - bar // 2, size // 2 + arm, size // 2 + bar // 2], fill=(255, 255, 255, 255))
    # Teal inner cross for the medical look.
    bar2, arm2 = 14, 40
    pen.rectangle([size // 2 - bar2 // 2, size // 2 - arm2, size // 2 + bar2 // 2, size // 2 + arm2], fill=(23, 184, 166, 255))
    pen.rectangle([size // 2 - arm2, size // 2 - bar2 // 2, size // 2 + arm2, size // 2 + bar2 // 2], fill=(23, 184, 166, 255))

    image = Image.alpha_composite(image, draw)

    # ECG trace across the middle.
    ecg = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    trace = ImageDraw.Draw(ecg)
    base = size // 2
    points = []
    x = 0
    pattern = [0, 0, 2, -2, 0, 0, 0, 10, -26, 34, -8, 0, 0, 2, -2, 0]
    while x < size:
        for step in pattern:
            points.append((x, base + step))
            x += 8
            if x >= size:
                break
    trace.line(points, fill=(57, 255, 158, 235), width=3, joint="curve")
    image = Image.alpha_composite(image, ecg)

    path = os.path.join(ROOT, "src", "main", "resources", "assets", "hospital-mod", "icon.png")
    image.save(path)
    return path


def main():
    for name, rows in ART.items():
        paint(name, rows)
    make_icon()
    print(f"painted {len(ART)} textures + icon")


if __name__ == "__main__":
    main()
