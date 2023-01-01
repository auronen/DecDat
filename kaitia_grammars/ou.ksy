meta:
  id: ou
  title: Compiled Output units
  file-extension: ou

  endian: le
doc: |
  OU class represents the whole zCCSLib serialized in OU.bin files
meta:

seq:
  - id: header
    type: header
  - id: archive_header
    type: archive_header
  - id: z_clib
    type: z_clib

types:
  header:
    seq:
      - id: magic
        type: ou_string
      - id: version
        type: ou_string
      - id: archiver_class
        type: ou_string
      - id: archiver_type
        type: ou_string
      - id: save_game
        type: ou_string
      - id: date
        type: ou_string
      - id: user
        type: ou_string
      - id: end
        type: ou_string

  property:
    seq:
      - id: type
        type: u1
      - id: prop_body
        type:
          switch-on: type
          cases:
            0x1: type_ou_string
            0x2: type_integer
            0x3: type_float
            0x11: type_enum
            0x12: type_hash
  z_clib:
    seq:
      - id: specifier
        type: property
      - id: type_1
        type: u1
      - id: hash_1
        type: u4 
      - id: type_2
        type: u1
      - id: num_of_items
        type: u4 
      - id: properties
        type: entry
        repeat: expr
        repeat-expr: num_of_items
        
  entry:
    seq:
      - id: properties
        type: property
        repeat: expr
        repeat-expr: 18
        
  archive_header:
    seq:
      - id: bs_version
        type: u4
      - id: object_count
        type: u4
      - id: hash_table_offset
        type: u4
    instances:
      ht:
        pos: hash_table_offset
        type: hash_table_header
        

  type_ou_string:
    seq:
      - id: size
        type: u2
      - id: object_id
        type: str
        size: size
        encoding: ASCII

  type_integer:
    seq:
      - id: value
        type: u4
  
  type_float:
    seq:
      - id: value
        type: f4
  
  
  type_enum:
    seq:
      - id: value
        type: u4
  
  type_hash:
    seq:
      - id: value
        type: u4

  hash_table_header:
    seq:
      - id: chunk_count
        type: u4
      - id: chunks
        type: hash_table_chunk
        repeat: expr
        repeat-expr: chunk_count

  hash_table_chunk:
    seq:
      - id: name_length
        type: u2
      - id: linear_value
        type: u2
      - id: hash_value
        type: u4
      - id: str
        type: str
        size: name_length
        encoding: ASCII

  ou_string:
    seq:
      - id: str
        type: str
        terminator: 0xa
        encoding: ASCII

  block:
    seq:
      - id: block_name
      - id: nom_of_block
      - id: blocName
      - id: 