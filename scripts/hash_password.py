#!/usr/bin/env python3
import hashlib
import secrets


def salt_for_password() -> str:
    return secrets.token_hex(16)


def hash_password_with_salt(password: str, salt: str) -> str:
    return hashlib.sha256((salt + password).encode()).hexdigest()


def main() -> None:
    password = input("Input Password: ")
    salt = salt_for_password()
    hashed = hash_password_with_salt(password, salt)

    print(f"Salt:   {salt}")
    print(f"Hash:   {hashed}")


if __name__ == "__main__":
    main()
