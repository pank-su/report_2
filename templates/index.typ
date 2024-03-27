#import "titlepage.typ": titlepage
#import "toc.typ": toc
#import "escd.typ": basicFrame


#let index(
  authors: (),
  title: "",
  body,
) = {
  set document(author: authors, title: title)


  // Структура
  // Титульный лист
  // titlepage(title: title,
  // authors: authors,
  // position: "Преподаватель",
  // education: "МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ РОССИЙСКОЙ ФЕДЕРАЦИИ\nфедеральное государственное автономное образовательное учреждение высшего образования\n«Санкт-Петербургский государственный университет аэрокосмического приборостроения»", group: "С021к", documentName: "ПОЯСНИТЕЛЬНАЯ ЗАПИСКА К КУРСОВОМУ ПРОЕКТУ", teachers: ("И.А Юрьева",), city: "Санкт-Петербург", discipline: "по дисциплине: МДК.03.01 Разарботка мобильных приложений", department: "ФАКУЛЬТЕТ СРЕДНЕГО ПРОФЕССИОНАЛЬНОГО ОБРАЗОВАНИЯ")
  // pagebreak()


  
  // Содержание
  toc()

  // pagebreak()

  // Настройки страниц.
  set text(font: "Times New Roman", size: 14pt, lang: "ru", region: "ru", hyphenate: true)
  set heading(numbering: "1.1")
  set page(margin: (left: 30mm, right: 20mm, top: 20mm, bottom: 25mm),  background: basicFrame())
  set par(justify: true, leading: 1.2em)
  // show heading: set block(below: 16pt, above: 32pt)
 
  show table: set text(hyphenate: true)
  show table: set par(justify: false, leading: 0.3em, first-line-indent: 0em)

  show figure.where(kind: image): set figure(supplement: "Рисунок")
  show figure.where(kind: table): set figure(supplement: "Таблица")


  //set figure(supplement: "Рисунок")
  show figure.where(kind: table): set figure.caption(position:  top)

  show figure.where(kind: table): it => [#align(left, [#it.caption #it.body \ ])]
  

  set figure.caption(separator: [ -- ])
  show figure.where(kind: raw): it => [#align(left, [#it.caption #it.body \ ])]


  let indent = 1.25cm
  set list(marker: [---], indent: indent, tight: true)
  show list: it => { 
   it
   par(text(size:0.1em, h(0.0em)))
  }
  set par(first-line-indent: indent)
  show heading: set text(size: 14pt, hyphenate: false)
  show heading.where(numbering: "1.1"): it => {

    stack(dir: ltr, h(indent), it)
    par(text(size:0.35em, h(0.0em)))
  }
  show heading.where(numbering: none): it => {
   it
   par(text(size:0.45em, h(0.0em)))
  }
  

  show heading.where(level: 1): it => [#pagebreak() #it]
  

  show "| ": it => [
  --- #h(5mm)
  ]


  // полу-утилита:
  show ref: it =>{
    let el = it.element

    if el != none and el.func() == figure {
      numbering(
      el.numbering,
       el.counter.at(el.location()).at(0)
      )
    } else {
      it
    }
    //repr(el.at("body"))
  } 

  body
}
