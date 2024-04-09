// o Expo Router sabe que este é um arquivo de configuração para as nossas rotas
// esse arquipo pode ser usado para centralizar configurações para toda a aplicação/rotas

import "@/styles/global.css"
import { Slot } from "expo-router"
import {
  useFonts,
  Roboto_700Bold,
  Roboto_500Medium,
  Roboto_400Regular
} from "@expo-google-fonts/roboto"
import { Loading } from "@/components/loading"

// Slot pega todas as nossas rotas e repassa aki

export default function Layout() {
  const [fontsLoaded] = useFonts({
    Roboto_700Bold,
    Roboto_500Medium,
    Roboto_400Regular
  })

  if(!fontsLoaded){
    return <Loading />
  }

  return <Slot />
}
