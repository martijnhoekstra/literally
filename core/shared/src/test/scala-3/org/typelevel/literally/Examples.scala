/*
 * Copyright 2021 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.typelevel.literally.examples

import scala.quoted.{Expr, Quotes}
import org.typelevel.literally.Literally

trait ShortString

object ShortString:
  val MaxLength = 10

  def fromString(value: String): Option[ShortString] =
    if value.length > 10 then None else Some(new ShortString { override def toString = value })

  object LiterallyInstance extends Literally[ShortString]:
    def validate(s: String): Option[String] =
      if ShortString.fromString(s).isDefined then None 
      else Some(s"ShortString must be <= ${ShortString.MaxLength}")

    def build(s: String)(using Quotes) =
      '{ShortString.fromString(${Expr(s)}).get}

extension (inline ctx: StringContext) inline def short(inline args: Any*): ShortString =
  ${ShortString.LiterallyInstance.impl('ctx, 'args)}
