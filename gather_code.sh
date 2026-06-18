#!/bin/bash

BACKEND_DIR="./backend"
OUTPUT_FILE="backend_analysis.txt"

echo "================ PROJECT STRUCTURE ================" > "$OUTPUT_FILE"

find "$BACKEND_DIR" \
    -type d \
    ! -path "*/target/*" \
    ! -path "*/.git/*" \
    | sort >> "$OUTPUT_FILE"

echo -e "\n\n================ JAVA FILES ================\n" >> "$OUTPUT_FILE"

find "$BACKEND_DIR" \
    -name "*.java" \
    ! -path "*/target/*" \
    ! -path "*/.git/*" \
    | sort | while read file
do
    echo -e "\n\n==================================================" >> "$OUTPUT_FILE"
    echo "FILE: $file" >> "$OUTPUT_FILE"
    echo "==================================================" >> "$OUTPUT_FILE"
    cat "$file" >> "$OUTPUT_FILE"
done

echo "Done. Output written to $OUTPUT_FILE"