# Library Management System

A Java command-line application for managing a library's book inventory.

## Features

- Add books
- Search books
- Check out books
- Return books
- Save library data to CSV
- Load library data from CSV

## Commands

add <Title> <Author> <ISBN-Number> <Year> <Copies>

checkout <ISBN>

return <ISBN>

findByTitleAndAuthor <Title> <Author>

list <ISBN>

save <filename>

load <filename>

print

exit

## Example

add Star_Trek Gene_Roddenberry ISBN-1234 1965 10

checkout ISBN-1234

list ISBN-1234

save library_data

exit
