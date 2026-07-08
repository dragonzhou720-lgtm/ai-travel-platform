from __future__ import annotations

import subprocess
from pathlib import Path

MYSQL_EXE = "mysql"
DB_HOST = "127.0.0.1"
DB_PORT = "3309"
DB_USER = "root"
DB_PASSWORD = "123456"
DB_NAME = "travel_platform"

ROOT = Path(__file__).resolve().parent
SEGMENT_DIR = ROOT / "segments"
FILES = [
    ROOT / "00_create_tables.sql",
    SEGMENT_DIR / "01_user.sql",
    SEGMENT_DIR / "02_attraction.sql",
    SEGMENT_DIR / "03_hotel.sql",
    SEGMENT_DIR / "04_route.sql",
    SEGMENT_DIR / "05_route_day.sql",
    SEGMENT_DIR / "06_favorite.sql",
]


def run_sql_file(path: Path) -> None:
    if not path.exists():
        raise FileNotFoundError(f"Missing SQL file: {path}")

    print(f"Importing {path.name} ...")
    content = path.read_text(encoding="utf-8")

    cmd = [
        MYSQL_EXE,
        "--default-character-set=utf8mb4",
        "-h",
        DB_HOST,
        "-P",
        DB_PORT,
        "-u",
        DB_USER,
        f"-p{DB_PASSWORD}",
        DB_NAME,
    ]

    result = subprocess.run(
        cmd,
        input=content,
        text=True,
        encoding="utf-8",
        errors="strict",
        capture_output=True,
    )

    if result.stdout:
        print(result.stdout, end="")
    if result.stderr:
        print(result.stderr, end="")
    if result.returncode != 0:
        raise RuntimeError(f"Import failed for {path.name} with exit code {result.returncode}")


def main() -> None:
    for file_path in FILES:
        run_sql_file(file_path)
    print("All SQL files imported successfully.")


if __name__ == "__main__":
    main()
