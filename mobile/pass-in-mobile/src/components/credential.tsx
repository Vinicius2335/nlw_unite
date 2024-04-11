import { Image, ImageBackground, View, Text, TouchableOpacity, useWindowDimensions } from "react-native"

// useWindowDimensions -> possui informaçoes da tela como altura/largura

import { Feather } from "@expo/vector-icons"
import { colors } from "@/styles/colors"
import { QRCode } from "@/components/qrcode"
import { BadgeStore } from "@/store/badge-store"
import { MotiView } from "moti"

// MotiView -> uma view animada

interface CredentialProps {
  data: BadgeStore
  image?: string
  onChangeAvatar?: () => void
  onExpandQRCode?: () => void
}

export function Credential({
  onChangeAvatar,
  onExpandQRCode,
  data
}: CredentialProps) {
  const { height } = useWindowDimensions()

  return (
    <MotiView 
    className="w-full self-stretch items-center"
    // ponto de partida da animação
    from={{
      opacity: 0,
      translateY: -height,
      rotateZ: "50deg",
      rotateY: "30deg",
      rotateX: "30deg"
    }}
    // para onde a animação vai
    animate={{
      opacity: 1,
      translateY: 0,
      rotateZ: "0deg",
      rotateY: "0deg",
      rotateX: "0deg"
    }}
    // configura a transição da animação
    transition={{
      // duration: 3000, não dá pra usar junto com damping
      type: "spring",
      damping: 20,
      rotateZ: {
        damping: 15,
        mass: 3
      }
    }}
    >
      <Image source={require("@/assets/ticket/band.png")} className="w-24 h-52 z-10" />

      <View className="bg-black/20 self-stretch items-center pb-6 border border-white/10 mx-3 rounded-2xl -mt-5">
        <ImageBackground
          className="px-6 py-8 h-40 items-center self-stretch border-b border-white/10 overflow-hidden"
          source={require("@/assets/ticket/header.png")}
        >
          {/* CREDENTIAL INFO */}
          <View className="w-full flex-row items-center justify-between ">
            <Text className="text-zinc-50 text-sm fond-bold">{data.eventTitle}</Text>
            <Text className="text-zinc-50 text-sm fond-bold">#{data.attendeeId}</Text>
          </View>

          <View className="w-40 h-40 bg-black rounded-full" />
        </ImageBackground>

        {/* *** IMAGE AVATAR USER *** */}
        {data.image ? (
          <TouchableOpacity activeOpacity={0.7} onPress={onChangeAvatar}>
            <Image source={{ uri: data.image }} className="w-36 h-36 rounded-full -mt-24" />
          </TouchableOpacity>
        ) : (
          <TouchableOpacity
            activeOpacity={0.7}
            className="w-36 h-36 rounded-full -mt-24 bg-gray-400 items-center justify-center"
            onPress={onChangeAvatar}
          >
            <Feather name="camera" color={colors.green[400]} size={32} />
          </TouchableOpacity>
        )}

        {/* *** USER INFO *** */}
        <Text className="text-zinc-50 font-bold text-2xl mt-4">{data.name}</Text>
        <Text className="text-zinc-300 font-regular text-base mb-4">{data.email}</Text>

        {/* *** QRCODE *** */}
        <QRCode value={data.checkInUrl} size={120} />

        <TouchableOpacity activeOpacity={0.7} className="mt-6" onPress={onExpandQRCode}>
          <Text className="font-body text-orange-500 text-sm">Ampliar QRCode</Text>
        </TouchableOpacity>
      </View>
    </MotiView>
  )
}
