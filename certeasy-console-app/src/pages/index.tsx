import { HardDrive } from "@/components/icons/hard-drive";
import { HardDrives } from "@/components/icons/hard-drives";
import { PaperCopy } from "@/components/icons/paper-copy";
import { PaperPlus } from "@/components/icons/paper-plus";
import { Tree } from "@/components/icons/tree";
import { FixedFilter } from "@/components/ui/fixed-filter";
import { IssuersList } from "@/features/issuers/components/issuers-list";
import { useIssuers, UseIssuersParams } from "@/hooks/issuers";
import { useQueryParams } from "@/hooks/query-params";
import { HStack, Button } from "@chakra-ui/react";

interface Value extends UseIssuersParams { layout?: 'TREE' | 'LIST' }

export default function HomePage() {
    const [params, setParams] = useQueryParams<Value>({ layout: 'LIST', type: '' })
    const onChange = (value: Value) => setParams(value, { replace: true })
    const { counts, issuers } = useIssuers(params)
    return (
        <>
            <HStack justify="space-between" wrap="wrap-reverse" py="2" pb="4" sm={{ py: "9" }} gapY="4">
                <FixedFilter
                    value={params}
                    onChange={onChange}
                    items={[
                        { label: 'All', value: { layout: 'LIST', type: '' }, count: counts.total },
                        { label: 'Root', value: { layout: 'LIST', type: 'ROOT' }, icon: HardDrive, count: counts.root },
                        { label: 'Sub', value: { layout: 'LIST', type: 'SUB_CA' }, icon: HardDrives, count: counts.subCA },
                        { label: 'Tree', value: { layout: 'TREE', type: '' }, icon: Tree }
                    ]}
                />
                <HStack wrap='wrap' flex="1" gapX="4" gapY="1.5" sm={{ flexWrap: 'nowrap' }} lg={{ justifyContent: 'end' }}>
                    <Button fontSize="lg" bg="primary">
                        New Root CA
                        <PaperPlus css={{ fontSize: '2xl' }} />
                    </Button>
                    <Button fontSize="lg" variant="outline" borderColor="primary" color="primary" >
                        Import CA
                        <PaperCopy css={{ fontSize: '2xl' }} />
                    </Button>
                </HStack>
            </HStack>
            <IssuersList issuers={issuers} />
        </>
    )
}