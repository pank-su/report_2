#let prilCount = counter("pril") // pril = приложение
#let alphabet = "а б в г д е ё ж з и й к л м н о п р с т у ф х ц ч ш щ ъ ы ь э ю я".split()


// center heading
#let ch(content) = {
  show heading: it => {
    text(it, size: 14pt)
  }
  align(heading(upper(content), numbering: none), center)
}

#let chn(content) = {
  show heading: it => {
    text(it, size: 14pt)
  }
  align(heading(content, numbering: none), center)
}

// image path
#let ip(path) = {
  return "images/" + path
}

// table fix
#let tf(content, caption) = {
  figure(content, caption: caption, supplement: "Таблица", kind: table)
}


#let pril(name) = {

  show heading: it => {
    text(it, size: 14pt)
  }

  align(heading([
    ПРИЛОЖЕНИЕ #upper(prilCount.display((..nums) => str(alphabet.at(nums.pos().last())))) #name
    ], numbering: none), center)

  prilCount.step()
}

// Код всех файлов из json (созданный утилитой tree)
#let allFiles(files, language: "kotlin", path: "/src/") = {
  if files.type == "file"{ 
    text( [ \ ] )
    text(files.name )
    text( [ \ ] )
    show raw: set text(hyphenate: false, size: 10pt)
    show raw: set par(justify: false, leading: 1em)
    raw(read(path + files.name), lang: language)
    return 
  }
  for content in (files.contents) {
  
    if content.type == "directory"{
      for content2 in (content.contents) {
        allFiles(content2, path: path, language: language)
      }
    } else if content.type == "file"{
      //allFiles()
    }
  }
}
