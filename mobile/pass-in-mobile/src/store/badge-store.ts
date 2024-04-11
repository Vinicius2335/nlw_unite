import { create } from "zustand"
import { createJSONStorage, persist } from "zustand/middleware"
import AsyncStorage from "@react-native-async-storage/async-storage"

// zustand -> relacionado com o estado global
// persist -> persistir as informaçoes
// createJSONStorage > para serializar o conteudo armazenamos string -> json
// AsyncStorage -> salvar informaçoes no dispositivo

export interface BadgeStore {
	name: string;
	email: string;
	checkInUrl: string;
	eventId: string;
	eventTitle: string;
  attendeeId: string;
  image?: string
}

type StateProps = {
  data: BadgeStore | null
  save: (data : BadgeStore) => void
  remove: () => void
  updateAvatar: (uri: string) => void
}

// 1. criamos um hook
// 2. pelo create podemos recuperar o método set para manipular/definir o valor/conteudo do nosso estado global
export const useBadgeStore = create(
  persist<StateProps>(
    (set) => ({
      // 3. definimos qual é o estado que teremos
      data: null,
      // 4: definimos um método para salvar o estado do conteudo
      save: (data : BadgeStore) => set(() => ({ data })),
      // 6: método para remover os dados salvos no dispositivo relacionado ao crachá
      remove: () => set(() => ({ data: null })),
      // 7: método para atualizar a imagem do usuário no crachá
      updateAvatar: (uri: string) => set((state) => ({ 
        data: state.data ? { ...state.data, image : uri} : state.data
       })),
    }),

    // 5: definimos as configuraçoes de AsyncStorage para persistir os dados no dispositivo
    { 
      name: "nlw-unite:badge",
      storage: createJSONStorage(() => AsyncStorage)
    }
  )
)