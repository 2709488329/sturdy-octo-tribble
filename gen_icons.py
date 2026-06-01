from PIL import Image, ImageDraw
import os

# Icon sizes for each density (px)
sizes = {
    "mdpi": 48,
    "hdpi": 72,
    "xhdpi": 96,
    "xxhdpi": 144,
    "xxxhdpi": 192
}

base = "/var/minis/workspace/PuppyAdoptWebApp/app/src/main/res"

for density, size in sizes.items():
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    cx, cy = size // 2, size // 2
    r = size * 0.42
    
    # Pink circle background
    draw.ellipse([cx-r, cy-r, cx+r, cy+r], fill=(244, 143, 177, 255))
    
    # Main pad (center, slightly lower)
    pr = size * 0.09
    draw.ellipse([
        cx - pr, cy - pr + int(size * 0.04),
        cx + pr, cy + pr + int(size * 0.04)
    ], fill=(236, 64, 122, 255))
    
    # Toe pads
    toe_positions = [
        (cx - int(size * 0.18), cy - int(size * 0.17)),
        (cx, cy - int(size * 0.22)),
        (cx + int(size * 0.18), cy - int(size * 0.17)),
        (cx - int(size * 0.12), cy + int(size * 0.05)),
    ]
    for tx, ty in toe_positions:
        tr = int(size * 0.07)
        draw.ellipse([tx-tr, ty-tr, tx+tr, ty+tr], fill=(236, 64, 122, 255))
    
    # Save
    dir_path = os.path.join(base, f"mipmap-{density}")
    os.makedirs(dir_path, exist_ok=True)
    img.save(os.path.join(dir_path, "ic_launcher.png"))
    img.save(os.path.join(dir_path, "ic_launcher_round.png"))
    print(f"Created {density} ({size}px)")

print("All icons generated!")
