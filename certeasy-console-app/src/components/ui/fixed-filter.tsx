import { IconType } from "@/components/icons/types";
import { HStack, StackProps, Button, Text, ButtonProps } from "@chakra-ui/react";

interface FixedFilerItemProps {
    icon?: IconType
    label: string
    count?: number,
    active?: boolean
    onClick?: () => unknown
}

function FixedFilerItem({ icon: Icon, label, active, count, onClick }: FixedFilerItemProps) {
    const color: ButtonProps['color'] = active ? 'white' : undefined
    const iconColor: ButtonProps['color'] = active ? 'white' : 'primary'
    const bgColor: ButtonProps['color'] = active ? 'primary' : undefined
    return (
        <HStack asChild>
            <Button data-active={`${active}`} variant="ghost" color={color} bgColor={bgColor} fontWeight="light" fontSize="lg" py="1" px="3.5" onClick={onClick}>
                {Icon && <Icon color={iconColor} h="8" w="8" />}
                <Text>{label}</Text>
                {(typeof count === 'number') && <Text>({count})</Text>}
            </Button>
        </HStack>
    )
}

type FixedButtonFilterProps<T extends object> = Omit<StackProps, 'children' | 'onChange'> & {
    value: T,
    items?: Omit<FixedFilerItemProps & { value: T }, 'active'>[]
    onChange?: (value: T) => unknown
}

function isActive<T extends object>(value: T, active: T) {
    for (const key in value) {
        if (value[key] !== active[key]) return false
    }
    return true
}

export function FixedFilter<T extends object>({ value, items = [], onChange, ...props }: FixedButtonFilterProps<T>) {
    return (
        <HStack asChild {...props} wrap="wrap">
            <ul>
                {items.map(({ value: v, ...props }, index) => (
                    <FixedFilerItem
                        key={index}
                        {...props}
                        active={isActive(v, value)}
                        onClick={() => onChange?.(v)}
                    />
                ))}
            </ul>
        </HStack>
    )
}