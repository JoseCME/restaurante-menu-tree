package gt.edu.miumg.app.mapper;

import gt.edu.miumg.app.openapi.model.NodeRequest;
import gt.edu.miumg.app.openapi.model.NodeResponse;
import gt.edu.miumg.engine.dto.NodeDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre NodeDTO (del tree-engine) y
 * NodeResponse/NodeRequest (del openapi.model)
 */
@Component
public class NodeMapper {

    /**
     * Convierte NodeDTO a NodeResponse recursivamente
     * @param nodeDTO el nodo DTO a convertir
     * @return el nodo convertido a NodeResponse
     */
    public NodeResponse dtoToResponse(NodeDTO nodeDTO) {
        if (nodeDTO == null) {
            return null;
        }

        NodeResponse response = new NodeResponse();
        response.setId(nodeDTO.getId());
        response.setValue(nodeDTO.getValue());
        response.setParentId(nodeDTO.getParentId());

        // Convertir recursivamente los hijos
        if (nodeDTO.getChildren() != null && !nodeDTO.getChildren().isEmpty()) {
            List<NodeResponse> childrenResponses = nodeDTO.getChildren()
                    .stream()
                    .map(this::dtoToResponse)
                    .collect(Collectors.toList());
            response.setChildren(childrenResponses);
        } else {
            response.setChildren(new ArrayList<>());
        }

        return response;
    }

    /**
     * Convierte una lista de NodeDTO a una lista de NodeResponse
     * @param nodeDTOs la lista de nodos DTO
     * @return la lista convertida a NodeResponse
     */
    public List<NodeResponse> dtoListToResponseList(List<NodeDTO> nodeDTOs) {
        if (nodeDTOs == null) {
            return new ArrayList<>();
        }

        return nodeDTOs.stream()
                .map(this::dtoToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte NodeRequest a NodeDTO
     * @param nodeRequest el nodo request a convertir
     * @return el nodo convertido a NodeDTO
     */
    public NodeDTO requestToDTO(NodeRequest nodeRequest) {
        if (nodeRequest == null) {
            return null;
        }

        NodeDTO dto = new NodeDTO();
        dto.setValue(nodeRequest.getValue());
        dto.setChildren(new ArrayList<>());

        return dto;
    }

    /**
     * Convierte NodeResponse a NodeDTO (solo para casos simples sin recursión)
     * @param nodeResponse el nodo response a convertir
     * @return el nodo convertido a NodeDTO
     */
    public NodeDTO responseToDTO(NodeResponse nodeResponse) {
        if (nodeResponse == null) {
            return null;
        }

        NodeDTO dto = new NodeDTO(
                nodeResponse.getId(),
                nodeResponse.getValue(),
                nodeResponse.getParentId()
        );

        if (nodeResponse.getChildren() != null && !nodeResponse.getChildren().isEmpty()) {
            List<NodeDTO> childrenDTOs = nodeResponse.getChildren()
                    .stream()
                    .map(this::responseToDTO)
                    .collect(Collectors.toList());
            dto.setChildren(childrenDTOs);
        }

        return dto;
    }
}
