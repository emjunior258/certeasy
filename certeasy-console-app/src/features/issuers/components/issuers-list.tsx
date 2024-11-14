import { Download } from "@/components/icons/download";
import { KeyShield } from "@/components/icons/key-shield";
import { KeyShieldError } from "@/components/icons/key-shield-error";
import { PlusSquare } from "@/components/icons/plus-square";
import { TrashCan } from "@/components/icons/trash-can";
import { IconType } from "@/components/icons/types";
import { EmptyState } from "@/components/ui/empty-state";
import { Pill } from "@/components/ui/pill";
import { Issuer } from "@/hooks/issuers";
import { Button, ButtonProps, HStack, SimpleGrid, Text, VStack } from "@chakra-ui/react";
import { useNavigate } from "react-router";

const typeMap: Record<Issuer['type'], string> = {
    'ROOT': 'Root',
    'SUB_CA': 'Sub',
}

interface IssuersListActionProps extends ButtonProps {
    icon: IconType
}

function IssuerListItemAction({ icon: Icon, color, ...props }: IssuersListActionProps) {
    return (
        <Button
            p="2"
            borderColor={`${color as string}/60`}
            bgColor={`${color as string}/8`}
            _hover={{ bgColor: color, color: 'white' }}
            color={color}
            variant="outline"
            {...props}
        >
            <Icon />
        </Button>
    )
}

interface IssuersListItemProps {
    issuer: Issuer
}

function IssuerListItem({ issuer }: IssuersListItemProps) {
    const navigate = useNavigate()
    const onClick = () => navigate(`/issuers/${issuer.id}`)
    return (
        <HStack
            onClick={onClick}
            asChild
            cursor="pointer"
            boxShadow='2px 6px 18px 0px {colors.black/8}'
            w="full"
            p="2"
            justify="space-between"
            borderRadius="sm"
            borderWidth="2px" borderColor="black/8"
            wrap="wrap"
            sm={{
                py: "5",
                pl: "10",
                pr: "5",
            }}
        >
            <li>
                <HStack gap="6">
                    <KeyShield color="primary" fontSize="3.25rem" />
                    <VStack align="start" gap="1">
                        <Text fontWeight="semibold">{issuer.name}</Text>
                        <Pill>{typeMap[issuer.type] ?? issuer.type}</Pill>
                    </VStack>
                </HStack>
                <SimpleGrid columns={2} w="full" gap="2" sm={{ w: 'unset' }}>
                    <IssuerListItemAction icon={Download} color="primary" w="full" />
                    <IssuerListItemAction icon={TrashCan} color="red" w="full" />
                </SimpleGrid>
            </li>
        </HStack>
    )
}

interface IssuersListProps {
    issuers: Issuer[]
}

export function IssuersList({ issuers }: IssuersListProps) {
    if (!issuers.length) return (
        <EmptyState
            icon={KeyShieldError}
            title="No certificate issuers created yet!"
            description="You can start by creating your first certificate issuer."
            action={{
                label: 'New Issuer',
                icon: PlusSquare
            }}
        />
    )
    return (
        <VStack asChild align="start">
            <ul>
                {issuers.map(issuer => (
                    <IssuerListItem key={issuer.id} issuer={issuer} />
                ))}
            </ul>
        </VStack>
    )
}