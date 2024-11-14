import { AlertCircle } from "@/components/icons/alert-circle";
import { Github } from "@/components/icons/github";
import { IconType } from "@/components/icons/types";
import { Container, ContainerProps, HStack, Link, Text, LinkProps, Box } from "@chakra-ui/react";

type FooterItemProps = Omit<LinkProps, 'asChild' | 'children'> & {
    label: string
    icon: IconType
}

function FooterItem({ label, icon: Icon, ...props }: FooterItemProps) {
    return (
        <li>
            <HStack asChild gap="2">
                <Link {...props} fontWeight="light" fontSize="sm" target="_blank" referrerPolicy="no-referrer">
                    <Icon css={{ fontSize: "1.625rem" }} />
                    <Text as="span">{label}</Text>
                </Link>
            </HStack>
        </li>
    )
}

type FooterProps = Omit<ContainerProps, 'asChild'>

export function Footer({ children, ...props }: FooterProps) {
    return (
        <Container asChild {...props} maxWidth="1250px">
            <footer>
                <HStack
                     py="7"
                    wrap="wrap"
                    asChild
                    role="navigation"
                    gapY="2"
                    gapX="6"
                    justify="center"
                    sm={{ justifyContent: 'end' }}
                    borderTopWidth="1px"
                    borderTopColor="black/30"
                >
                    <ul>
                        <FooterItem icon={Github} label="Read Documentation" href="https://github.com/emjunior258/certeasy" />
                        <FooterItem icon={AlertCircle} label="Report Issue" href="https://github.com/emjunior258/certeasy/issues/new" color="red" />
                        {children}
                    </ul>
                </HStack>
            </footer>
        </Container>
    )
}

Footer.Item = FooterItem