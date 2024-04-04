import { ComponentProps } from "react"

interface TableProps extends ComponentProps<'table'> {

}

export function Table(props : TableProps){
  return (
     // não é possivel criar arredondamento em tabelas, precisamos criar uma div por volta da tabela
      <div className="border border-white/10 rounded-lg">
        <table {...props} className="w-full" />
      </div>
  )
}

