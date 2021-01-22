package com.xantrix.webapp.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "listini")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "dettListini")
@Builder
public class Listini implements Serializable {

	private static final long serialVersionUID = -5250045976635683173L;
	@Id
	@NotNull(message = "{NotNull.Listini.id.Validation}")
	private String id;
	@Size(min = 3, max = 30, message = "{Size.Listini.descrizione.Validation}")
	private String descrizione;
	@NotNull(message = "{NotNull.Listini.obsoleto.Validation}")
	private Obsoleto obsoleto;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "listino", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<DettListini> dettListini;

	public Listini(String id, String descrizione, Obsoleto obsoleto) {
		super();
		this.id = id;
		this.descrizione = descrizione;
		this.obsoleto = obsoleto;
	}

	public Listini(String id, String descrizione, String obsoleto) {
		super();
		this.id = id;
		this.descrizione = descrizione;
		if (StringUtils.isNotEmpty(obsoleto))
			this.obsoleto = Obsoleto.fromValue(obsoleto);
	}

	@Getter
	@AllArgsConstructor
	public enum Obsoleto {

		SI("Si"), NO("No");

		private String value;

		public static Obsoleto fromValue(String value) {
			return Arrays.stream(Obsoleto.values())
					.filter(s -> s.value.equalsIgnoreCase(value))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Unexpected value '" + value + "'"));
		}

		@Override
		public String toString() {
			return getValue();
		}
	}
}
