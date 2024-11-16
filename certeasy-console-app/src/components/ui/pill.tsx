import { TextProps, Text } from "@chakra-ui/react";

type PillProps = TextProps

export function Pill(props: PillProps) {
    return (
        <Text bg="secondary" fontSize="xs" fontWeight="light" py="0.5" px="2.5" borderRadius="xl" {...props} />
    )
}