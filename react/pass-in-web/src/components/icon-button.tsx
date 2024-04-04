import { ComponentProps } from "react"

interface IconButtonProps extends ComponentProps<"button"> {
  transparent?: boolean
}

export function IconButton({ transparent, ...props }: IconButtonProps) {
  return (
    // assim permite o children tbm igual no nav-link
    // forma mais reduzida de se fazer
    <button 
    {...props}
    className={`${transparent ? "bg-black/20" : "bg-white/10"} border border-white/10 rounded-md p-1.5`}
    />
  )
}
